package com.MemoryManager.model.memory;

import com.MemoryManager.model.config.Configuration;
import com.MemoryManager.model.systemResources.Page;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainMemory {

    private int size;
    private ArrayList<Page> frames;
    private boolean isFull;

    public MainMemory(){
        this.size = 0;
        this.isFull = false;
        this.frames = null;
    }

    public void prepareFrames(int frameSize){
        int numberOfFrames = this.size / frameSize;
        this.frames = new ArrayList<Page>(numberOfFrames);
        for (int i = 0; i < numberOfFrames; i++){
            this.frames.add(null);
        }
    }

    public void removePage(Page p){
        int indexToRemove = -1;
        for(int i = 0; i < this.frames.size(); i++){
            if(this.frames.get(i).getPageId() == p.getPageId() && this.frames.get(i).getFatherId() == p.getFatherId()){
                indexToRemove = i;
                p.setPresenceBit(false);
                p.setUseBit(false);
                break;
            }
        }
        if(indexToRemove != -1){
            this.frames.set(indexToRemove, null);
        }
        else{
            // treat exception
        }

    }

    public int findFrame(Page p){
        int indexToReturn = -1;
        for(int i = 0; i < this.frames.size(); i++){
            if(this.frames.get(i).getPageId() == p.getPageId() && this.frames.get(i).getFatherId() == p.getFatherId()){
                indexToReturn = i;
                break;
            }
        }
        return indexToReturn;
    }

    public void receiveSinglePage(Page processPage){
        this.checkNullPosition();
        if(!this.isFull){
            int emptyFrame = this.findEmptyFrame();
            if(emptyFrame != -1){
                processPage.setUseBit(true);
                processPage.setPresenceBit(true);
                this.frames.set(emptyFrame, processPage);
            }
            else{
                // treat exception
            }
        }
        else{
            // treat exception
        }

    }

    public int findEmptyFrame(){
        if(!this.isFull){
            for(int i = 0; i < this.frames.size(); i++){
                if(this.frames.get(i) == null){
                    return i;
                }
            }
        }
        return -1;
    }

    public void checkNullPosition(){
        for(Page frame : this.frames){
            if(frame == null){
                this.isFull = false;
                return;
            }
        }
        this.isFull = true;
    }

}
