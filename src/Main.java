import java.io.*;
import java.util.*;
import java.io.IOException;

class RentCD{
    String Name;
    int ID;
    boolean isRent=false;
    RentCD(String Name,int id){
        this.Name=Name;
        this.ID=id;
    }
}
class SellCD{
    String Name;
    int count;
    SellCD(String Name,int count){
        this.Name=Name;
        this.count=count;
    }
}
public class Main extends Thread {
    Vector <RentCD> vRCD=new Vector<RentCD>();
    Vector <SellCD> vSCD=new Vector<SellCD>();
    Main(){
        for(int i=1;i<11;i++){
            vRCD.add(new RentCD("RentCD"+i,i));
            vSCD.add(new SellCD("SellCD"+i,10));
        }
    }
    public void sale() throws InterruptedException {
        Random r=new Random();
        int where=r.nextInt(10);//买走第i本书
        int num=r.nextInt(5)+1;
        SellCD thisCD=vSCD.get(where);
        if(thisCD.count<num){
            int judge=r.nextInt(2);//1为等待,0为离开
            if(judge==1){//立即进货
                System.out.println(new Date()+" Shop restore");
                for(int i=0;i<10;i++) vSCD.get(i).count=10;
            }
        }
        if(thisCD.count>=num){
            if(num==1) System.out.println(new Date()+" "+num+" "+thisCD.Name+" are bought");
            else System.out.println(new Date()+" "+num+" "+thisCD.Name+"s are bought");
            thisCD.count-=num;
        }
    }
    public void rent() throws InterruptedException {
        Random r=new Random();
        int where=r.nextInt(10);//借走第i本书
        RentCD thisCD=vRCD.get(where);
        if(thisCD.isRent){
            int judge=r.nextInt(2);//1为等待,0为离开
            if(judge==1){
                while(thisCD.isRent){
                    Thread.sleep(150);
                }
            }
        }
        if(!thisCD.isRent) {
            System.out.println(new Date()+" "+thisCD.Name + " is borrowed");
            thisCD.isRent = true;
            long t = r.nextInt(100) + 200;
            Thread.sleep(t);
            System.out.println(new Date()+" "+thisCD.Name + " is returned");
            thisCD.isRent = false;
        }
    }
    public static void main(String[] args) throws IOException {
        PrintStream ps=new PrintStream(new FileOutputStream("record.txt"));
        System.setOut(ps);
        Random r=new Random();
        Main CDShop=new Main();
        long sumTime=0;//每一秒钟补一次货
        long start = System.currentTimeMillis( );
        long end = System.currentTimeMillis( );
        while(end-start<120000) {
            long Wait=r.nextInt(200);
            sumTime+=Wait;
            Thread Rent1 = new Thread(new Runnable() {
                public void run() {
                        try { CDShop.rent(); } catch (InterruptedException e) {  }
                }
            }, "Rent1");
            Thread Rent2 = new Thread(new Runnable() {
                public void run() {
                    try {
                        CDShop.rent();
                    } catch (InterruptedException e) {
                    }
                }
            }, "Rent2");
            Thread Sale1 = new Thread(new Runnable() {
                public void run() {
                    try {
                        CDShop.sale();
                    } catch (InterruptedException e) {
                    }
                }
            }, "Sale1");
            Thread Sale2 = new Thread(new Runnable() {
                public void run() {
                    try {
                        CDShop.sale();
                    } catch (InterruptedException e) {
                    }
                }
            }, "Sale2");
            Rent1.start();
            Sale1.start();
            Rent2.start();
            Sale2.start();
            try{Thread.sleep(Wait);}catch (InterruptedException e){}
            if(sumTime>=1000){
                System.out.println(new Date()+" Shop restore");
                for(int i=0;i<10;i++) CDShop.vSCD.get(i).count=10;
                sumTime=0;
            }
            end=System.currentTimeMillis( );
        }
    }
}
