package com.entity.model;

import com.entity.JifenjiluEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 积分记录
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class JifenjiluModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 用户姓名
     */
    private Integer yonghuId;


    /**
     * 原因
     */
    private String jifenjiluName;


    /**
     * 积分数量
     */
    private Double jifenjiluNumber;


    /**
     * 类型
     */
    private Integer jifenTypes;


    /**
     * 插入时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 设置：主键
	 */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：用户姓名
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 设置：用户姓名
	 */
    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：原因
	 */
    public String getJifenjiluName() {
        return jifenjiluName;
    }


    /**
	 * 设置：原因
	 */
    public void setJifenjiluName(String jifenjiluName) {
        this.jifenjiluName = jifenjiluName;
    }
    /**
	 * 获取：积分数量
	 */
    public Double getJifenjiluNumber() {
        return jifenjiluNumber;
    }


    /**
	 * 设置：积分数量
	 */
    public void setJifenjiluNumber(Double jifenjiluNumber) {
        this.jifenjiluNumber = jifenjiluNumber;
    }
    /**
	 * 获取：类型
	 */
    public Integer getJifenTypes() {
        return jifenTypes;
    }


    /**
	 * 设置：类型
	 */
    public void setJifenTypes(Integer jifenTypes) {
        this.jifenTypes = jifenTypes;
    }
    /**
	 * 获取：插入时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 设置：插入时间
	 */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
