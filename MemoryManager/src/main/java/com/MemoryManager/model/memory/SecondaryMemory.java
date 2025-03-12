package com.MemoryManager.model.memory;

import com.MemoryManager.model.config.Configuration;
import com.MemoryManager.model.systemResources.Page;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecondaryMemory {

    private int size;
    private ArrayList<Page> frames;
    private boolean isFull;

    public SecondaryMemory() {
        this.size = 0;
        this.frames = null;
        this.isFull = false;
    }

    public void prepareFrames(int frameSize){
        int numberOfFrames = this.size / frameSize;
        this.frames = new ArrayList<Page>(numberOfFrames);
        for (int i = 0; i < numberOfFrames; i++){
            this.frames.add(null);
        }
    }

    public void receiveProcessPages(ArrayList<Page> processPages){
        int nullPositions = this.countNullPositions();
        if(nullPositions >= processPages.size()){
            for(Page p: processPages){
                int emptyFrame = this.findEmptyFrame();
                if(emptyFrame != -1){
                    this.frames.set(emptyFrame, p);
                }
                else{
                    // treat exception
                }
            }
        }
        else{
            // treat exception
        }

    }

    public void receiveSinglePage(Page processPage){
        this.checkNullPosition();
        if(!this.isFull){
            int emptyFrame = this.findEmptyFrame();
            if(emptyFrame != -1){
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

    public void removePage(Page p){
        int indexToRemove = -1;
        for(int i = 0; i < this.frames.size(); i++){
            if(this.frames.get(i).getPageId() == p.getPageId() && this.frames.get(i).getFatherId() == p.getFatherId()){
                indexToRemove = i;
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

    public void checkNullPosition(){
        for(Page frame : this.frames){
            if(frame == null){
                this.isFull = false;
                return;
            }
        }
        this.isFull = true;
    }

    public int countNullPositions(){
        int count = 0;
        for(Page frame : this.frames){
            if(frame == null){
                count += 1;
            }
        }
        if (count == 0){
            this.isFull = true;
        }
        return count;
    }
}
