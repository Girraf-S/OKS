package com.example.oks.data;

import Exceptions.PackageException;
import communication.Consumer;

import java.util.ArrayDeque;
import java.util.Deque;

public class Buffer {
    private static final Deque<Byte> deque = new ArrayDeque<>();

    public static int add(byte[] buffer) {

        for (int i = 0; i < buffer.length; i++) {
            if (!deque.add(buffer[i])) return i;
        }
        return buffer.length;
    }
    public static void addFirst(byte b){
        deque.addFirst(b);
    }

    public static Byte poll() {
        return deque.pollFirst();
    }
    public static boolean isEmpty() {
        return deque.isEmpty();
    }
    public static Deque<Byte> getDeque(){
        return deque;
    }
}

