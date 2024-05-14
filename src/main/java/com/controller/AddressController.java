package com.controller;












import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.StringUtil;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 收货地址
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/address")
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service
    @Autowired
    private YonghuService yonghuService;



    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = addressService.queryPage(params);

        //字典表数据转换
        List<AddressView> list =(List<AddressView>)page.getList();
        for(AddressView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        AddressEntity address = addressService.selectById(id);
        if(address !=null){
            //entity转view
            AddressView view = new AddressView();
            BeanUtils.copyProperties( address , view );//把实体数据重构到view中

                //级联表
                YonghuEntity yonghu = yonghuService.selectById(address.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody AddressEntity address, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,address:{}",this.getClass().getName(),address.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("用户".equals(role))
            address.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        Wrapper<AddressEntity> queryWrapper = new EntityWrapper<AddressEntity>()
            .eq("yonghu_id", address.getYonghuId())
            .eq("address_name", address.getAddressName())
            .eq("address_phone", address.getAddressPhone())
            .eq("address_dizhi", address.getAddressDizhi())
            .eq("isdefault_types", address.getIsdefaultTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        AddressEntity addressEntity = addressService.selectOne(queryWrapper);
        if(addressEntity==null){
            address.setInsertTime(new Date());
            address.setCreateTime(new Date());
            Integer isdefaultTypes = address.getIsdefaultTypes();
            if(isdefaultTypes == 2 ){//如果当前的是默认地址，把当前用户的其他改为不是默认地址
                List<AddressEntity> addressEntitys = addressService.selectList(new EntityWrapper<AddressEntity>().eq("isdefault_types",2));
                if(addressEntitys != null && addressEntitys.size()>0){
                    for(AddressEntity a:addressEntitys)
                        a.setIsdefaultTypes(1);
                    addressService.updateBatchById(addressEntitys);
                }
            }
            addressService.insert(address);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody AddressEntity address, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,address:{}",this.getClass().getName(),address.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("用户".equals(role))
            address.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        //根据字段查询是否有相同数据
        Wrapper<AddressEntity> queryWrapper = new EntityWrapper<AddressEntity>()
            .notIn("id",address.getId())
            .andNew()
            .eq("yonghu_id", address.getYonghuId())
            .eq("address_name", address.getAddressName())
            .eq("address_phone", address.getAddressPhone())
            .eq("address_dizhi", address.getAddressDizhi())
            .eq("isdefault_types", address.getIsdefaultTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        AddressEntity addressEntity = addressService.selectOne(queryWrapper);
        address.setUpdateTime(new Date());
        if(addressEntity==null){
            //  String role = String.valueOf(request.getSession().getAttribute("role"));
            //  if("".equals(role)){
            //      address.set
            //  }
            Integer isdefaultTypes = address.getIsdefaultTypes();
            if(isdefaultTypes == 2 ){//如果当前的是默认地址，把当前用户的其他改为不是默认地址
                List<AddressEntity> addressEntitys = addressService.selectList(new EntityWrapper<AddressEntity>().eq("isdefault_types",2));
                if(addressEntitys != null && addressEntitys.size()>0){
                    for(AddressEntity a:addressEntitys)
                        a.setIsdefaultTypes(1);
                    addressService.updateBatchById(addressEntitys);
                }
            }
            addressService.updateById(address);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        addressService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }




    /**
    * 前端列表
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));

        // 没有指定排序字段就默认id倒序
        if(StringUtil.isEmpty(String.valueOf(params.get("orderBy")))){
            params.put("orderBy","id");
        }
        PageUtils page = addressService.queryPage(params);

        //字典表数据转换
        List<AddressView> list =(List<AddressView>)page.getList();
        for(AddressView c:list)
            dictionaryService.dictionaryConvert(c); //修改对应字典表字段
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        AddressEntity address = addressService.selectById(id);
            if(address !=null){
                //entity转view
                AddressView view = new AddressView();
                BeanUtils.copyProperties( address , view );//把实体数据重构到view中

                //级联表
                    YonghuEntity yonghu = yonghuService.selectById(address.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody AddressEntity address, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,address:{}",this.getClass().getName(),address.toString());
        Wrapper<AddressEntity> queryWrapper = new EntityWrapper<AddressEntity>()
            .eq("yonghu_id", address.getYonghuId())
            .eq("address_name", address.getAddressName())
            .eq("address_phone", address.getAddressPhone())
            .eq("address_dizhi", address.getAddressDizhi())
            .eq("isdefault_types", address.getIsdefaultTypes())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        AddressEntity addressEntity = addressService.selectOne(queryWrapper);
        if(addressEntity==null){
            address.setInsertTime(new Date());
            address.setCreateTime(new Date());
        //  String role = String.valueOf(request.getSession().getAttribute("role"));
        //  if("".equals(role)){
        //      address.set
        //  }
            Integer isdefaultTypes = address.getIsdefaultTypes();
            if(isdefaultTypes == 2 ){//如果当前的是默认地址，把当前用户的其他改为不是默认地址
                List<AddressEntity> addressEntitys = addressService.selectList(new EntityWrapper<AddressEntity>().eq("isdefault_types",2));
                if(addressEntitys != null && addressEntitys.size()>0){
                    for(AddressEntity a:addressEntitys)
                        a.setIsdefaultTypes(1);
                    addressService.updateBatchById(addressEntitys);
                }
            }
        addressService.insert(address);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }




}

