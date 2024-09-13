package org.shopping.utils;

import org.modelmapper.ModelMapper;

public class ConvertUtils {
    public static <T> T convert(Object source, Class<T> dstClass){
        if(source==null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper.map(source, dstClass);
    }
}
