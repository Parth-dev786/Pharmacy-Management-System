package com.college.pharmacy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class PharmacyFrame extends JFrame {
    private final DataStore store;
    private JTable medicineTable,supplierTable,expiryTable,reportTable;
    private DefaultTableModel medicineModel,supplierModel,expiryModel,reportModel;
    private JTextField mName,mSupplier,mBatch,mQty,mPrice,mExpiry,mReorder,searchField;
    private JComboBox<String> mCategory;
    private JTextField sName,sPhone,sEmail,sAddress;
    private JTextField saleMedicineId,saleQty,customerName;
    private JTextArea receipt;

    public PharmacyFrame(DataStore store){
        this.store=store;setTitle("Pharmacy Management System");setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120,740);setLocationRelativeTo(null);setLayout(new BorderLayout(8,8));
        JLabel title=new JLabel("PHARMACY MANAGEMENT SYSTEM",SwingConstants.CENTER);
        title.setFont(new Font("SansSerif",Font.BOLD,23));title.setBorder(BorderFactory.createEmptyBorder(12,8,8,8));add(title,BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane();tabs.addTab("Medicine Master",medicinePanel());tabs.addTab("Expiry Tracking",expiryPanel());
        tabs.addTab("Sales Billing",salesPanel());tabs.addTab("Supplier Records",supplierPanel());tabs.addTab("Stock Report",reportPanel());
        add(tabs,BorderLayout.CENTER);
        JLabel footer=new JLabel("Local file storage: "+store.getDataFile(),SwingConstants.CENTER);footer.setFont(new Font("SansSerif",Font.PLAIN,10));add(footer,BorderLayout.SOUTH);
        refreshAll();
    }
    private JPanel medicinePanel(){
        JPanel p=new JPanel(new BorderLayout(8,8));JPanel form=new JPanel(new GridLayout(4,4,8,6));
        mName=field(form,"Medicine name");mCategory=new JComboBox<>(new String[]{"Tablet","Syrup","Injection"});addCombo(form,"Category",mCategory);
        mSupplier=field(form,"Supplier ID (optional; use 0 if none)");mBatch=field(form,"Batch number");mQty=field(form,"Quantity");
        mPrice=field(form,"Unit price (INR)");mExpiry=field(form,"Expiry (YYYY-MM-DD)");mReorder=field(form,"Reorder level");
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));JButton add=new JButton("Add Medicine"),update=new JButton("Update Selected"),del=new JButton("Delete Selected"),clear=new JButton("Clear");
        buttons.add(add);buttons.add(update);buttons.add(del);buttons.add(clear);JPanel top=new JPanel(new BorderLayout());top.add(form);top.add(buttons,BorderLayout.SOUTH);
        medicineModel=model("ID","Name","Category","Supplier ID","Batch","Quantity","Price","Expiry","Reorder");medicineTable=new JTable(medicineModel);medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicineTable.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())fillMedicineForm();});
        add.addActionListener(e->addMedicine());update.addActionListener(e->updateMedicine());del.addActionListener(e->deleteMedicine());clear.addActionListener(e->clearMedicineForm());
        p.add(top,BorderLayout.NORTH);p.add(new JScrollPane(medicineTable),BorderLayout.CENTER);return p;
    }
    private JPanel expiryPanel(){
        JPanel p=new JPanel(new BorderLayout(8,8));JLabel note=new JLabel("Expired medicines and medicines expiring within the next 30 days",SwingConstants.CENTER);
        JButton refresh=new JButton("Refresh");refresh.addActionListener(e->refreshExpiry());expiryModel=model("ID","Medicine","Quantity","Expiry Date","Status");expiryTable=new JTable(expiryModel);
        p.add(note,BorderLayout.NORTH);p.add(new JScrollPane(expiryTable),BorderLayout.CENTER);p.add(refresh,BorderLayout.SOUTH);return p;
    }
    private JPanel salesPanel(){
        JPanel p=new JPanel(new BorderLayout(8,8));JPanel form=new JPanel(new GridLayout(3,2,8,8));saleMedicineId=field(form,"Medicine ID");saleQty=field(form,"Quantity");customerName=field(form,"Customer name");
        JButton bill=new JButton("Generate Bill & Update Stock");receipt=new JTextArea(13,50);receipt.setEditable(false);receipt.setFont(new Font(Font.MONOSPACED,Font.PLAIN,13));
        bill.addActionListener(e->generateBill());p.add(form,BorderLayout.NORTH);p.add(bill,BorderLayout.CENTER);p.add(new JScrollPane(receipt),BorderLayout.SOUTH);return p;
    }
    private JPanel supplierPanel(){
        JPanel p=new JPanel(new BorderLayout(8,8));JPanel form=new JPanel(new GridLayout(2,4,8,8));sName=field(form,"Supplier name");sPhone=field(form,"Phone");sEmail=field(form,"Email");sAddress=field(form,"Address");
        JButton add=new JButton("Add Supplier");add.addActionListener(e->addSupplier());JPanel top=new JPanel(new BorderLayout());top.add(form);top.add(add,BorderLayout.SOUTH);
        supplierModel=model("ID","Name","Phone","Email","Address");supplierTable=new JTable(supplierModel);p.add(top,BorderLayout.NORTH);p.add(new JScrollPane(supplierTable),BorderLayout.CENTER);return p;
    }
    private JPanel reportPanel(){
        JPanel p=new JPanel(new BorderLayout(8,8));JPanel bar=new JPanel(new FlowLayout(FlowLayout.LEFT));searchField=new JTextField(22);JButton search=new JButton("Search"),refresh=new JButton("Show All"),export=new JButton("Export CSV");
        bar.add(new JLabel("Medicine name:"));bar.add(searchField);bar.add(search);bar.add(refresh);bar.add(export);
        search.addActionListener(e->searchMedicines());searchField.addKeyListener(new KeyAdapter(){@Override public void keyReleased(KeyEvent e){searchMedicines();}});
        refresh.addActionListener(e->{searchField.setText("");refreshMedicines();});export.addActionListener(e->exportCsv());
        reportModel=model("ID","Name","Category","Supplier ID","Batch","Quantity","Price","Expiry","Reorder");reportTable=new JTable(reportModel);
        p.add(bar,BorderLayout.NORTH);p.add(new JScrollPane(reportTable),BorderLayout.CENTER);return p;
    }
    private JTextField field(JPanel p,String label){JPanel cell=new JPanel(new BorderLayout(2,2));cell.add(new JLabel(label),BorderLayout.NORTH);JTextField f=new JTextField();cell.add(f);p.add(cell);return f;}
    private void addCombo(JPanel p,String label,JComboBox<String> combo){JPanel cell=new JPanel(new BorderLayout(2,2));cell.add(new JLabel(label),BorderLayout.NORTH);cell.add(combo);p.add(cell);}
    private DefaultTableModel model(String...cols){return new DefaultTableModel(cols,0){@Override public boolean isCellEditable(int r,int c){return false;}};}
    private void addMedicine(){try{MedicineInput x=readMedicineInput();int id=store.addMedicine(x.category,x.name,x.supplierId,x.batch,x.qty,x.price,x.expiry,x.reorder);refreshAll();clearMedicineForm();info("Medicine added. ID: "+id);}catch(Exception ex){error(ex);}}
    private void updateMedicine(){int row=medicineTable.getSelectedRow();if(row<0){info("Select a medicine row first.");return;}try{MedicineInput x=readMedicineInput();int id=(int)medicineModel.getValueAt(row,0);store.updateMedicine(id,x.name,x.supplierId,x.batch,x.qty,x.price,x.expiry,x.reorder);refreshAll();info("Medicine updated.");}catch(Exception ex){error(ex);}}
    private void deleteMedicine(){int row=medicineTable.getSelectedRow();if(row<0){info("Select a medicine row first.");return;}int id=(int)medicineModel.getValueAt(row,0);if(JOptionPane.showConfirmDialog(this,"Delete selected medicine?","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try{store.deleteMedicine(id);refreshAll();clearMedicineForm();}catch(Exception ex){error(ex);}}
    private MedicineInput readMedicineInput(){
        String name=mName.getText().trim();if(name.isEmpty())throw new IllegalArgumentException("Medicine name is required.");
        int supplier=parseIntOrZero(mSupplier.getText(),"Supplier ID"),qty=Integer.parseInt(mQty.getText().trim()),reorder=Integer.parseInt(mReorder.getText().trim());
        double price=Double.parseDouble(mPrice.getText().trim());if(qty<0||reorder<0||price<0)throw new IllegalArgumentException("Quantity, reorder level and price cannot be negative.");
        if(supplier<0)throw new IllegalArgumentException("Supplier ID cannot be negative.");
        return new MedicineInput(name,(String)mCategory.getSelectedItem(),supplier,mBatch.getText().trim(),qty,price,LocalDate.parse(mExpiry.getText().trim()),reorder);
    }
    private int parseIntOrZero(String s,String label){if(s==null||s.trim().isEmpty())return 0;int v=Integer.parseInt(s.trim());if(v<0)throw new IllegalArgumentException(label+" cannot be negative.");return v;}
    private void fillMedicineForm(){int r=medicineTable.getSelectedRow();if(r<0)return;mName.setText(cell(r,1));mCategory.setSelectedItem(cell(r,2));mSupplier.setText(cell(r,3));mBatch.setText(cell(r,4));mQty.setText(cell(r,5));mPrice.setText(cell(r,6));mExpiry.setText(cell(r,7));mReorder.setText(cell(r,8));}
    private String cell(int r,int c){Object v=medicineModel.getValueAt(r,c);return v==null?"":v.toString();}
    private void clearMedicineForm(){mName.setText("");mSupplier.setText("");mBatch.setText("");mQty.setText("");mPrice.setText("");mExpiry.setText("");mReorder.setText("");medicineTable.clearSelection();}
    private void addSupplier(){try{String name=sName.getText().trim();if(name.isEmpty())throw new IllegalArgumentException("Supplier name is required.");int id=store.addSupplier(name,sPhone.getText().trim(),sEmail.getText().trim(),sAddress.getText().trim());sName.setText("");sPhone.setText("");sEmail.setText("");sAddress.setText("");refreshSuppliers();info("Supplier added. ID: "+id);}catch(Exception ex){error(ex);}}
    private void generateBill(){try{int id=Integer.parseInt(saleMedicineId.getText().trim()),qty=Integer.parseInt(saleQty.getText().trim());Medicine m=store.findMedicine(id);if(m==null)throw new IllegalArgumentException("Medicine ID not found.");String customer=customerName.getText().trim();if(customer.isEmpty())customer="Walk-in customer";store.createSale(id,qty,customer);double total=Math.round(m.getPrice()*qty*100.0)/100.0;Sale sale=store.getSales().get(store.getSales().size()-1);receipt.setText("          PHARMACY SALES BILL\n"+"------------------------------------------\nBill No: "+sale.getId()+"\nDate: "+sale.getDate()+"\nCustomer: "+customer+"\nMedicine: "+m.getName()+"\nDosage: "+m.getDosageInformation()+"\nQuantity: "+qty+"\nRate: Rs. "+m.getPrice()+"\n------------------------------------------\nTOTAL: Rs. "+total+"\n------------------------------------------\nThank you!");refreshAll();}catch(Exception ex){error(ex);}}
    private void refreshAll(){refreshMedicines();refreshSuppliers();refreshExpiry();refreshReport();}
    private void refreshMedicines(){if(medicineModel==null)return;medicineModel.setRowCount(0);for(Medicine m:store.getMedicines())medicineModel.addRow(medicineRow(m));}
    private void refreshSuppliers(){if(supplierModel==null)return;supplierModel.setRowCount(0);for(Supplier s:store.getSuppliers())supplierModel.addRow(new Object[]{s.getId(),s.getName(),s.getPhone(),s.getEmail(),s.getAddress()});}
    private void refreshExpiry(){if(expiryModel==null)return;expiryModel.setRowCount(0);LocalDate today=LocalDate.now();for(Medicine m:store.getExpiryItems()){String status=m.isExpired(today)?"EXPIRED":"EXPIRING SOON";expiryModel.addRow(new Object[]{m.getId(),m.getName(),m.getQuantity(),m.getExpiryDate(),status});}}
    private void refreshReport(){if(reportModel==null)return;reportModel.setRowCount(0);for(Medicine m:store.getMedicines())reportModel.addRow(medicineRow(m));}
    private Object[] medicineRow(Medicine m){return new Object[]{m.getId(),m.getName(),m.getCategory(),m.getSupplierId()==0?"":m.getSupplierId(),m.getBatchNo(),m.getQuantity(),String.format("%.2f",m.getPrice()),m.getExpiryDate(),m.getReorderLevel()};}
    private void searchMedicines(){if(reportModel==null)return;String q=searchField.getText().trim().toLowerCase();reportModel.setRowCount(0);for(Medicine m:store.getMedicines())if(m.getName().toLowerCase().contains(q))reportModel.addRow(medicineRow(m));}
    private void exportCsv(){JFileChooser chooser=new JFileChooser();chooser.setSelectedFile(new File("pharmacy_stock.csv"));if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;try(PrintWriter out=new PrintWriter(new FileWriter(chooser.getSelectedFile()))){out.println("ID,Name,Category,Supplier ID,Batch,Quantity,Price,Expiry,Reorder Level");for(Medicine m:store.getMedicines())out.printf("%d,%s,%s,%d,%s,%d,%.2f,%s,%d%n",m.getId(),csv(m.getName()),m.getCategory(),m.getSupplierId(),csv(m.getBatchNo()),m.getQuantity(),m.getPrice(),m.getExpiryDate(),m.getReorderLevel());info("CSV report exported successfully.");}catch(Exception ex){error(ex);}}
    private String csv(String s){return "\""+(s==null?"":s.replace("\"","\"\""))+"\"";}
    private void info(String s){JOptionPane.showMessageDialog(this,s);}
    private void error(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage()==null?ex.toString():ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    private record MedicineInput(String name,String category,int supplierId,String batch,int qty,double price,LocalDate expiry,int reorder){}
}
