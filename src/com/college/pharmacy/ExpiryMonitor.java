package com.college.pharmacy;
public class ExpiryMonitor extends Thread {
    private final DataStore store;
    public ExpiryMonitor(DataStore store){this.store=store;setName("Daily-Expiry-Monitor");setDaemon(true);}
    @Override public void run(){
        while(!isInterrupted()){
            long expired=store.getExpiryItems().stream().filter(m->m.isExpired(java.time.LocalDate.now())).count();
            long soon=store.getExpiryItems().stream().filter(m->!m.isExpired(java.time.LocalDate.now())).count();
            System.out.println("[Expiry check] Expired: "+expired+" | Expiring within 30 days: "+soon);
            try{Thread.sleep(24L*60*60*1000);}catch(InterruptedException ex){interrupt();}
        }
    }
}
