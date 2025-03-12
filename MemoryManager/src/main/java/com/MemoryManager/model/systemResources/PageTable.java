package com.MemoryManager.model.systemResources;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageTable {

    private Process owner;

    public PageTable(Process owner){
        this.owner = owner;
    }


}
