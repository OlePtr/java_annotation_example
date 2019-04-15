package com.oleptr.test.core;

public class Phone {
private String name;

@RandomInt(min =1, max =120)
public int coreNumber;

@RandomInt(min =1, max =120)
private int ram;


 public Phone (String name, int coreNumber){
     this.name = name;
     this.coreNumber = coreNumber;
 }
 public Phone(){
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
