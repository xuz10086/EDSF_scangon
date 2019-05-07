package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

/**
 * 版本
 *
 * @author xuz
 * @date 2019/1/11 2:16 PM
 * @param
 * @return
 */
public class Ver implements Serializable {

    private static final long serialVersionUID = -6760845703700343623L;
    private String lMaterialsVerId;
    private String vcMaterialsVerName;
    // 物料料位定义id
    private String lMaterialsDefinitionId;

    public String getlMaterialsVerId() {
        return lMaterialsVerId;
    }

    public void setlMaterialsVerId(String lMaterialsVerId) {
        this.lMaterialsVerId = lMaterialsVerId;
    }

    public String getVcMaterialsVerName() {
        return vcMaterialsVerName;
    }

    public void setVcMaterialsVerName(String vcMaterialsVerName) {
        this.vcMaterialsVerName = vcMaterialsVerName;
    }

    public String getlMaterialsDefinitionId() {
        return lMaterialsDefinitionId;
    }

    public void setlMaterialsDefinitionId(String lMaterialsDefinitionId) {
        this.lMaterialsDefinitionId = lMaterialsDefinitionId;
    }

    @Override
    public String toString() {
        return "Ver{" +
                "lMaterialsVerId='" + lMaterialsVerId + '\'' +
                ", vcMaterialsVerName='" + vcMaterialsVerName + '\'' +
                ", lMaterialsDefinitionId='" + lMaterialsDefinitionId + '\'' +
                '}';
    }
}
