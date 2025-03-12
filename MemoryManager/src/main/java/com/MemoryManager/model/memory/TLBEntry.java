package com.MemoryManager.model.memory;

import com.MemoryManager.model.systemResources.Page;

public class TLBEntry {

    private int validityBit;
    private int pageNumber; // get the page numbers through the process page arraylist
    private Page page; // page table entry
}
