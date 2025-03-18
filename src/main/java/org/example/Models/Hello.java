package org.example.Models;

import com.sun.jna.Library;
import com.sun.jna.Native;


public interface Hello extends Library{
    Hello INSTANCE = (Hello) Native.load("hello", Hello.class);
    String sayHello();
}
