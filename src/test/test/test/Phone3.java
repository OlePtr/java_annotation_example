package test.test.test;

import com.oleptr.test.core.RandomInt;

public class Phone3 {
private String name;

@RandomInt(min =1, max =120)
public int coreNumber;

@RandomInt(min =1, max =120)
private int ram;


 public Phone3(String name, int coreNumber){
     this.name = name;
     this.coreNumber = coreNumber;
 }
 public Phone3(){
     this.name = "generic";
     this.coreNumber = 100;


 }
 public int getCoreNumber() {
     return coreNumber;
 }
    public String getName() {
        return name;
    }
}
