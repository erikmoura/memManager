package com.MemoryManager.model.systemResources;

import com.MemoryManager.model.memory.MainMemory;
import com.MemoryManager.model.memory.SecondaryMemory;

public interface ReplacementPolicy {
    void replace(Page p, MainMemory mainMemo, SecondaryMemory secMemo);

}
