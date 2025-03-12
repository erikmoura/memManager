package com.MemoryManager.model.memory;

import java.util.ArrayList;

public class Translator {

    public ArrayList<Integer> findPageAddress(int address, int pageSize){
        int frameStart = address / pageSize;
        int offSet = address % pageSize;

        ArrayList<Integer> toReturn = new ArrayList<>(2);
        toReturn.add(frameStart);
        toReturn.add(offSet);

        return toReturn;
    }


}
