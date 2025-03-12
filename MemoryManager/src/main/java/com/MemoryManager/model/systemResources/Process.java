package com.MemoryManager.model.systemResources;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Process {

    private int id;
    private State state;
    private int size;
    private int numOfPages;
    private ArrayList<Page> pages;

    public Process(int id, State state, int size, int numOfPages, ArrayList<Page> pages){

    }


}
