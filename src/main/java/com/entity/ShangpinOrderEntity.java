package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 商品订单
 *
 * @author 
 * @email
 */
@TableName("shangpin_order")
public class ShangpinOrderEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public ShangpinOrderEntity() {

	}

	public ShangpinOrderEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")

    private Integer id;


    /**
     * 订单号
     */
    @TableField(value = "shangpin_order_uuid_number")

    private String shangpinOrderUuidNumber;


    /**
     * 收获地址
     */
    @TableField(value = "address_id")

    private Integer addressId;


    /**
     * 商品
     */
    @TableField(value = "shangpin_id")

    private Integer shangpinId;


    /**
     * 用户
     */
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 购买数量
     */
    @TableField(value = "buy_number")

    private Integer buyNumber;


    /**
     * 实付价格
     */
    @TableField(value = "shangpin_order_true_price")

    private Double shangpinOrderTruePrice;


    /**
     * 订单类型
     */
    @TableField(value = "shangpin_order_types")

    private Integer shangpinOrderTypes;


    /**
     * 收货方式
     */
    @TableField(value = "shouhuo_types")

    private Integer shouhuoTypes;


    /**
     * 支付类型
     */
    @TableField(value = "shangpin_order_payment_types")

    private Integer shangpinOrderPaymentTypes;


    /**
     * 订单创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
     * 发货单位
     */
    @TableField(value = "fahuodanwei")
    private String fahuodanwei;

    /**
     * 发货人
     */
    @TableField(value = "fahuoren")
    private String fahuoren;

    /**
     * 联系方式
     */
    @TableField(value = "lianxifangshi")
    private String lianxifangshi;

    public String getFahuodanwei() {
        return fahuodanwei;
    }

    public void setFahuodanwei(String fahuodanwei) {
        this.fahuodanwei = fahuodanwei;
    }

    public String getFahuoren() {
        return fahuoren;
    }

    public void setFahuoren(String fahuoren) {
        this.fahuoren = fahuoren;
    }

    public String getLianxifangshi() {
        return lianxifangshi;
    }

    public void setLianxifangshi(String lianxifangshi) {
        this.lianxifangshi = lianxifangshi;
    }

    /**
	 * 设置：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 获取：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 设置：订单号
	 */
    public String getShangpinOrderUuidNumber() {
        return shangpinOrderUuidNumber;
    }


    /**
	 * 获取：订单号
	 */

    public void setShangpinOrderUuidNumber(String shangpinOrderUuidNumber) {
        this.shangpinOrderUuidNumber = shangpinOrderUuidNumber;
    }
    /**
	 * 设置：收获地址
	 */
    public Integer getAddressId() {
        return addressId;
    }


    /**
	 * 获取：收获地址
	 */

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    /**
	 * 设置：商品
	 */
    public Integer getShangpinId() {
        return shangpinId;
    }


    /**
	 * 获取：商品
	 */

    public void setShangpinId(Integer shangpinId) {
        this.shangpinId = shangpinId;
    }
    /**
	 * 设置：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 获取：用户
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 设置：购买数量
	 */
    public Integer getBuyNumber() {
        return buyNumber;
    }


    /**
	 * 获取：购买数量
	 */

    public void setBuyNumber(Integer buyNumber) {
        this.buyNumber = buyNumber;
    }
    /**
	 * 设置：实付价格
	 */
    public Double getShangpinOrderTruePrice() {
        return shangpinOrderTruePrice;
    }


    /**
	 * 获取：实付价格
	 */

    public void setShangpinOrderTruePrice(Double shangpinOrderTruePrice) {
        this.shangpinOrderTruePrice = shangpinOrderTruePrice;
    }
    /**
	 * 设置：订单类型
	 */
    public Integer getShangpinOrderTypes() {
        return shangpinOrderTypes;
    }


    /**
	 * 获取：订单类型
	 */

    public void setShangpinOrderTypes(Integer shangpinOrderTypes) {
        this.shangpinOrderTypes = shangpinOrderTypes;
    }
    /**
	 * 设置：收货方式
	 */
    public Integer getShouhuoTypes() {
        return shouhuoTypes;
    }


    /**
	 * 获取：收货方式
	 */

    public void setShouhuoTypes(Integer shouhuoTypes) {
        this.shouhuoTypes = shouhuoTypes;
    }
    /**
	 * 设置：支付类型
	 */
    public Integer getShangpinOrderPaymentTypes() {
        return shangpinOrderPaymentTypes;
    }


    /**
	 * 获取：支付类型
	 */

    public void setShangpinOrderPaymentTypes(Integer shangpinOrderPaymentTypes) {
        this.shangpinOrderPaymentTypes = shangpinOrderPaymentTypes;
    }
    /**
	 * 设置：订单创建时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 获取：订单创建时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 设置：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 获取：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ShangpinOrder{" +
            "id=" + id +
            ", shangpinOrderUuidNumber=" + shangpinOrderUuidNumber +
            ", addressId=" + addressId +
            ", shangpinId=" + shangpinId +
            ", yonghuId=" + yonghuId +
            ", buyNumber=" + buyNumber +
            ", shangpinOrderTruePrice=" + shangpinOrderTruePrice +
            ", shangpinOrderTypes=" + shangpinOrderTypes +
            ", shouhuoTypes=" + shouhuoTypes +
            ", shangpinOrderPaymentTypes=" + shangpinOrderPaymentTypes +
            ", insertTime=" + insertTime +
            ", createTime=" + createTime +
        "}";
    }
}
