package com.MemoryManager.model.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configuration {

    private int mainMemo_size = 4096; // 4KB = 2^2 * 2^10 = 2^12 = 4096 bytes
    private int secMemo_size = 16384; // 16KB = 2^4 * 2^10 = 2^14 = 16384 bytes
    private int process_Maxsize = 16384; // tamanho do endereco logico = tamanho da memoria secundaria
    private int pageFrame_size = 1024; // 1KB

    private Configuration(){}
    private static volatile Configuration instance;
    public static Configuration getInstance(){
        if( instance == null){
            synchronized(Configuration.class){
                if(instance == null){
                    instance = new Configuration();
                }
            }
        }
        return instance;
    }

}
