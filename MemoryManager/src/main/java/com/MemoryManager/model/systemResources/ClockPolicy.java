package com.MemoryManager.model.systemResources;

import com.MemoryManager.model.memory.MainMemory;
import com.MemoryManager.model.memory.SecondaryMemory;

public class ClockPolicy implements ReplacementPolicy {

    private int startPoint;

    public ClockPolicy(){
        this.startPoint = 0;
    }
    @Override
    public void replace(Page p, MainMemory mainMemo, SecondaryMemory secMemo) {
        // first step
        int count = 0;
        while(count < mainMemo.getFrames().size()){

            if(this.startPoint + count < mainMemo.getFrames().size()){
                if(mainMemo.getFrames().get(this.startPoint+count) == null){
                    mainMemo.receiveSinglePage(p);
                    this.startPoint = this.startPoint + count + 1;
                    return;
                }
                if(!(mainMemo.getFrames().get(this.startPoint+count).isUseBit()) && !(mainMemo.getFrames().get(this.startPoint+count).isModifiedBit())){
                    // select the frame and do the replacement
                    secMemo.receiveSinglePage(mainMemo.getFrames().get(this.startPoint+count));
                    mainMemo.removePage(mainMemo.getFrames().get(this.startPoint+count));
                    mainMemo.receiveSinglePage(p);
                    this.startPoint = this.startPoint + count + 1;
                    return;
                }
            }
            else{
                for(int i = 0; i < this.startPoint; i++){
                    if(mainMemo.getFrames().get(i) == null){
                        mainMemo.receiveSinglePage(p);
                        this.startPoint = i + 1;
                        return;
                    }
                    if(!(mainMemo.getFrames().get(i).isUseBit()) && !(mainMemo.getFrames().get(i).isModifiedBit())){
                        // select the frame and do the replacement
                        secMemo.receiveSinglePage(mainMemo.getFrames().get(i));
                        mainMemo.removePage(mainMemo.getFrames().get(i));
                        mainMemo.receiveSinglePage(p);
                        this.startPoint = i + 1;
                        return;
                    }
                }
                break;
            }
            count += 1;
        }

        //second step
        count = 0;
        while(count < mainMemo.getFrames().size()){

            if(this.startPoint + count < mainMemo.getFrames().size()){
                if(mainMemo.getFrames().get(this.startPoint+count) == null){
                    mainMemo.receiveSinglePage(p);
                    this.startPoint = this.startPoint + count + 1;
                    return;
                }
                if(!(mainMemo.getFrames().get(this.startPoint+count).isUseBit()) && (mainMemo.getFrames().get(this.startPoint+count).isModifiedBit())){
                    // select the frame and do the replacement
                    secMemo.receiveSinglePage(mainMemo.getFrames().get(this.startPoint+count));
                    mainMemo.removePage(mainMemo.getFrames().get(this.startPoint+count));
                    mainMemo.receiveSinglePage(p);
                    this.startPoint = this.startPoint + count + 1;
                    return;
                }
                // set the use bits to zero
                mainMemo.getFrames().get(this.startPoint+count).setUseBit(false);
            }
            else{
                for(int i = 0; i < this.startPoint; i++){
                    if(mainMemo.getFrames().get(i) == null){
                        mainMemo.receiveSinglePage(p);
                        this.startPoint = i + 1;
                        return;
                    }
                    if(!(mainMemo.getFrames().get(i).isUseBit()) && (mainMemo.getFrames().get(i).isModifiedBit())){
                        // select the frame and do the replacement
                        secMemo.receiveSinglePage(mainMemo.getFrames().get(i));
                        mainMemo.removePage(mainMemo.getFrames().get(i));
                        mainMemo.receiveSinglePage(p);
                        this.startPoint = i + 1;
                        return;
                    }
                    // set the use bits to zero
                    mainMemo.getFrames().get(i).setUseBit(false);
                }
                break;
            }
            count += 1;
        }

        // third step
        this.replace(p, mainMemo, secMemo);

    }
}
