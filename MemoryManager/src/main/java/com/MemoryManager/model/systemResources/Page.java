package com.MemoryManager.model.systemResources;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    private int fatherId;
    private boolean presenceBit;
    private boolean modifiedBit;
    private boolean useBit; // for clock policy
    private int frameNumber;
    private int pageId;

    public Page(int pageId, int fatherId){
        this.fatherId = fatherId;
        this.pageId = pageId;
        this.presenceBit = false;
        this.modifiedBit = false;
        this.useBit = false;
        this.frameNumber = -1;
    }

}
