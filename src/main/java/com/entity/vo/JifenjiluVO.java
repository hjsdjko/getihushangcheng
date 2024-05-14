package com.entity.vo;

import com.entity.JifenjiluEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 积分记录
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 */
@TableName("jifenjilu")
public class JifenjiluVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */

    @TableField(value = "id")
    private Integer id;


    /**
     * 用户姓名
     */

    @TableField(value = "yonghu_id")
    private Integer yonghuId;


    /**
     * 原因
     */

    @TableField(value = "jifenjilu_name")
    private String jifenjiluName;


    /**
     * 积分数量
     */

    @TableField(value = "jifenjilu_number")
    private Double jifenjiluNumber;


    /**
     * 类型
     */

    @TableField(value = "jifen_types")
    private Integer jifenTypes;


    /**
     * 插入时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat

    @TableField(value = "insert_time")
    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat

    @TableField(value = "create_time")
    private Date createTime;


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
	 * 设置：用户姓名
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 获取：用户姓名
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 设置：原因
	 */
    public String getJifenjiluName() {
        return jifenjiluName;
    }


    /**
	 * 获取：原因
	 */

    public void setJifenjiluName(String jifenjiluName) {
        this.jifenjiluName = jifenjiluName;
    }
    /**
	 * 设置：积分数量
	 */
    public Double getJifenjiluNumber() {
        return jifenjiluNumber;
    }


    /**
	 * 获取：积分数量
	 */

    public void setJifenjiluNumber(Double jifenjiluNumber) {
        this.jifenjiluNumber = jifenjiluNumber;
    }
    /**
	 * 设置：类型
	 */
    public Integer getJifenTypes() {
        return jifenTypes;
    }


    /**
	 * 获取：类型
	 */

    public void setJifenTypes(Integer jifenTypes) {
        this.jifenTypes = jifenTypes;
    }
    /**
	 * 设置：插入时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 获取：插入时间
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

}
