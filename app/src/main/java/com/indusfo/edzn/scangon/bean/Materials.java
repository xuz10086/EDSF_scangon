package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

/**
 * 原材料
 *
 * @author xuz
 * @date 2019/1/11 2:15 PM
 */
public class Materials implements Serializable {

    private static final long serialVersionUID = -4940713975223717080L;

    // id
    private String lMaterialsId;
    // 物料编码
    private String vcMaterialsCode;
    // 物料名称
    private String vcMaterialsModel;
    // 产品型号
    private String vcProductModel;

    public String getlMaterialsId() {
        return lMaterialsId;
    }

    public void setlMaterialsId(String lMaterialsId) {
        this.lMaterialsId = lMaterialsId;
    }

    public String getVcMaterialsCode() {
        return vcMaterialsCode;
    }

    public void setVcMaterialsCode(String vcMaterialsCode) {
        this.vcMaterialsCode = vcMaterialsCode;
    }

    public String getVcMaterialsModel() {
        return vcMaterialsModel;
    }

    public void setVcMaterialsModel(String vcMaterialsModel) {
        this.vcMaterialsModel = vcMaterialsModel;
    }

    public String getVcProductModel() {
        return vcProductModel;
    }

    public void setVcProductModel(String vcProductModel) {
        this.vcProductModel = vcProductModel;
    }

    @Override
    public String toString() {
        return "Materials{" +
                "lMaterialsId='" + lMaterialsId + '\'' +
                ", vcMaterialsCode='" + vcMaterialsCode + '\'' +
                ", vcMaterialsModel='" + vcMaterialsModel + '\'' +
                ", vcProductModel='" + vcProductModel + '\'' +
                '}';
    }
}
