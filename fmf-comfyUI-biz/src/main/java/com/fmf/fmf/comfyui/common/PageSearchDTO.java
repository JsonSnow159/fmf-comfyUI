package com.fmf.fmf.comfyui.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PageSearchDTO implements Serializable {
    private Integer page;
    private Integer size;
    private Integer offset;

    public Integer getPage() {
        if (this.page == null) {
            return 1;
        }
        return this.page;
    }

    public Integer getSize() {
        if (this.size == null) {
            return 15;
        }
        return this.size;
    }

    public Integer getOffset() {

        if (this.offset != null) {
            return this.offset;
        }
        return (getPage()-1)*getSize();
    }
}