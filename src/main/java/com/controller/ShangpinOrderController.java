package com.controller;












import java.math.BigDecimal;
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
 * 商品订单
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/shangpinOrder")
public class ShangpinOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ShangpinOrderController.class);

    @Autowired
    private ShangpinOrderService shangpinOrderService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShangpinService shangpinService;
    @Autowired
    private YonghuService yonghuService;
@Autowired
private CartService cartService;
@Autowired
private JifenjiluService jifenjiluService;
@Autowired
private ShangpinCommentbackService shangpinCommentbackService;



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
        PageUtils page = shangpinOrderService.queryPage(params);

        //字典表数据转换
        List<ShangpinOrderView> list =(List<ShangpinOrderView>)page.getList();
        for(ShangpinOrderView c:list){
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
        ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
        if(shangpinOrder !=null){
            //entity转view
            ShangpinOrderView view = new ShangpinOrderView();
            BeanUtils.copyProperties( shangpinOrder , view );//把实体数据重构到view中

                //级联表
                AddressEntity address = addressService.selectById(shangpinOrder.getAddressId());
                if(address != null){
                    BeanUtils.copyProperties( address , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setAddressId(address.getId());
                }
                //级联表
                ShangpinEntity shangpin = shangpinService.selectById(shangpinOrder.getShangpinId());
                if(shangpin != null){
                    BeanUtils.copyProperties( shangpin , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setShangpinId(shangpin.getId());
                }
                //级联表
                YonghuEntity yonghu = yonghuService.selectById(shangpinOrder.getYonghuId());
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
    public R save(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("用户".equals(role))
            shangpinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        shangpinOrder.setInsertTime(new Date());
        shangpinOrder.setCreateTime(new Date());
        shangpinOrderService.insert(shangpinOrder);
        return R.ok();
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("用户".equals(role))
            shangpinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        //根据字段查询是否有相同数据
        Wrapper<ShangpinOrderEntity> queryWrapper = new EntityWrapper<ShangpinOrderEntity>()
            .eq("id",0)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ShangpinOrderEntity shangpinOrderEntity = shangpinOrderService.selectOne(queryWrapper);
        if(shangpinOrderEntity==null){
            //  String role = String.valueOf(request.getSession().getAttribute("role"));
            //  if("".equals(role)){
            //      shangpinOrder.set
            //  }
            shangpinOrderService.updateById(shangpinOrder);//根据id更新
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
        shangpinOrderService.deleteBatchIds(Arrays.asList(ids));
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
        PageUtils page = shangpinOrderService.queryPage(params);

        //字典表数据转换
        List<ShangpinOrderView> list =(List<ShangpinOrderView>)page.getList();
        for(ShangpinOrderView c:list)
            dictionaryService.dictionaryConvert(c); //修改对应字典表字段
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
            if(shangpinOrder !=null){
                //entity转view
                ShangpinOrderView view = new ShangpinOrderView();
                BeanUtils.copyProperties( shangpinOrder , view );//把实体数据重构到view中

                //级联表
                    AddressEntity address = addressService.selectById(shangpinOrder.getAddressId());
                if(address != null){
                    BeanUtils.copyProperties( address , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setAddressId(address.getId());
                }
                //级联表
                    ShangpinEntity shangpin = shangpinService.selectById(shangpinOrder.getShangpinId());
                if(shangpin != null){
                    BeanUtils.copyProperties( shangpin , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setShangpinId(shangpin.getId());
                }
                //级联表
                    YonghuEntity yonghu = yonghuService.selectById(shangpinOrder.getYonghuId());
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
    public R add(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder.toString());
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if("用户".equals(role)){
            ShangpinEntity shangpinEntity = shangpinService.selectById(shangpinOrder.getShangpinId());
            if(shangpinEntity == null){
                return R.error(511,"查不到该物品");
            }
//            Double shangpinNewMoney = shangpinEntity.getShangpinNewMoney();

            if(false){
            }
            else if((shangpinEntity.getShangpinKucunNumber() -shangpinOrder.getBuyNumber())<0){
                return R.error(511,"购买数量不能大于库存数量");
            }
            else if(shangpinEntity.getShangpinNewMoney() == null){
                return R.error(511,"物品价格不能为空");
            }

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");
            double balance = yonghuEntity.getNewMoney() - shangpinEntity.getShangpinNewMoney()*shangpinOrder.getBuyNumber();//余额
            if(balance<0)
                return R.error(511,"余额不够支付");
            shangpinOrder.setShangpinOrderTypes(3); //设置订单状态为已支付
                shangpinOrder.setShangpinOrderPaymentTypes(1);
                shangpinOrder.setInsertTime(new Date());
                shangpinOrder.setCreateTime(new Date());
                shangpinOrderService.insert(shangpinOrder);//新增订单
            yonghuEntity.setNewMoney(balance);
            yonghuService.updateById(yonghuEntity);
            return R.ok();
        }else{
            return R.error(511,"您没有权限支付订单");
        }
    }

    /**
     * 添加订单
     */
    @RequestMapping("/order")
    public R add(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("order方法:,,Controller:{},,params:{}",this.getClass().getName(),params.toString());
        String shangpinOrderUuidNumber = String.valueOf(new Date().getTime());

        //获取当前登录用户的id
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer shouhuoTypes = Integer.valueOf(String.valueOf(params.get("shouhuoTypes")));//收货方式
        Integer addressId = Integer.valueOf(String.valueOf(params.get("addressId")));
        String data = String.valueOf(params.get("shangpins"));
        JSONArray jsonArray = JSON.parseArray(data);
        List<Map> shangpins = JSON.parseObject(jsonArray.toString(), List.class);

        //获取当前登录用户的个人信息
        YonghuEntity yonghuEntity = yonghuService.selectById(userId);

        //积分记录表
        List<JifenjiluEntity> jifenjiluList = new ArrayList<>();
        //当前订单表
        List<ShangpinOrderEntity> shangpinOrderList = new ArrayList<>();
        //商品表
        List<ShangpinEntity> shangpinList = new ArrayList<>();
        //购物车ids
        List<Integer> cartIds = new ArrayList<>();

        // 获取折扣
        Wrapper<DictionaryEntity> dictionary = new EntityWrapper<DictionaryEntity>()
                .eq("dic_code", "huiyuandengji_types")
                .eq("dic_name", "会员等级类型名称")
                .eq("code_index", yonghuEntity.getHuiyuandengjiTypes())
                ;
        DictionaryEntity dictionaryEntity = dictionaryService.selectOne(dictionary);
        BigDecimal zhekou = new BigDecimal(1.0);
        if(dictionaryEntity != null ){
            zhekou = BigDecimal.valueOf(Double.valueOf(dictionaryEntity.getBeizhu()));
        }

        //循环取出需要的数据
        for (Map<String, Object> map : shangpins) {
            //取值
            Integer shangpinId = Integer.valueOf(String.valueOf(map.get("shangpinId")));//商品
            Integer buyNumber = Integer.valueOf(String.valueOf(map.get("buyNumber")));//购买数量
            Integer shangpinOrderPaymentTypes = Integer.valueOf(String.valueOf(params.get("shangpinOrderPaymentTypes")));//支付类型

            ShangpinEntity shangpinEntity = shangpinService.selectById(shangpinId);
            String id = String.valueOf(map.get("id"));
            if(StringUtil.isNotEmpty(id))
                cartIds.add(Integer.valueOf(id));

            if(shangpinEntity.getShangpinKucunNumber()< buyNumber)
                return R.error(shangpinEntity.getShangpinName()+"的库存不足");


            //设置数据
            //在积分记录表增加记录
            JifenjiluEntity jifenjiluEntity = new JifenjiluEntity();
            //订单信息表增加数据
            ShangpinOrderEntity shangpinOrderEntity = new ShangpinOrderEntity<>();

            //赋值订单信息
            shangpinOrderEntity.setShangpinOrderUuidNumber(shangpinOrderUuidNumber);//订单号
            shangpinOrderEntity.setAddressId(addressId);//收获地址
            shangpinOrderEntity.setShangpinId(shangpinId);//商品
            shangpinOrderEntity.setYonghuId(userId);//用户
            shangpinOrderEntity.setBuyNumber(buyNumber);//购买数量 ？？？？？？
            shangpinOrderEntity.setShangpinOrderTypes(3);//订单类型
            shangpinOrderEntity.setShouhuoTypes(shouhuoTypes);//收货方式 ？？？？？？
            shangpinOrderEntity.setShangpinOrderPaymentTypes(shangpinOrderPaymentTypes);//支付类型
            shangpinOrderEntity.setInsertTime(new Date());//订单创建时间
            shangpinOrderEntity.setCreateTime(new Date());//创建时间


            //判断商品的库存是否足够
            if(shangpinEntity.getShangpinKucunNumber() < buyNumber){
                //商品库存不足直接返回
                return R.error(shangpinEntity.getShangpinName()+"的库存不足");
            }else{
                //商品库存充足就减库存
                shangpinEntity.setShangpinKucunNumber(shangpinEntity.getShangpinKucunNumber() - buyNumber);
            }

            //判断是什么支付方式 1代表余额 2代表积分
            if(shangpinOrderPaymentTypes == 1){//余额支付
                //计算金额
                Double money = new BigDecimal(shangpinEntity.getShangpinNewMoney()).multiply(new BigDecimal(buyNumber)).multiply(zhekou).doubleValue();

                if(yonghuEntity.getNewMoney() - money <0 ){
                    return R.error("余额不足,请充值！！！");
                }else{
                    yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() - money); //设置金额
                    yonghuEntity.setYonghuSumJifen(yonghuEntity.getYonghuSumJifen() + money); //设置总积分
                    yonghuEntity.setYonghuNewJifen(yonghuEntity.getYonghuNewJifen() + money); //设置现积分

                        if(yonghuEntity.getYonghuSumJifen()  < 1000)
                            yonghuEntity.setHuiyuandengjiTypes(1);
                        else if(yonghuEntity.getYonghuSumJifen()  < 10000)
                            yonghuEntity.setHuiyuandengjiTypes(2);
                        else if(yonghuEntity.getYonghuSumJifen()  < 100000)
                            yonghuEntity.setHuiyuandengjiTypes(3);

                    jifenjiluEntity.setCreateTime(new Date());
                    jifenjiluEntity.setYonghuId(userId);
                    jifenjiluEntity.setInsertTime(new Date());
                    jifenjiluEntity.setJifenTypes(2);
                    jifenjiluEntity.setJifenjiluNumber(money);
                    jifenjiluEntity.setJifenjiluName("购买 "+ shangpinEntity.getShangpinName()+" "+buyNumber+" 个 花费金额 "+jifenjiluEntity.getJifenjiluNumber());

                    shangpinOrderEntity.setShangpinOrderTruePrice(money);

                }
            }
            else{//积分支付

                Double money = shangpinEntity.getShangpinNewMoney() * buyNumber;
                if(yonghuEntity.getYonghuNewJifen() - money <0 ){
                    return R.error("积分不足,无法兑换");
                }else{
                    yonghuEntity.setYonghuNewJifen(yonghuEntity.getYonghuNewJifen() - money);//设置现在积分

                    jifenjiluEntity.setCreateTime(new Date());
                    jifenjiluEntity.setYonghuId(userId);
                    jifenjiluEntity.setInsertTime(new Date());
                    jifenjiluEntity.setJifenTypes(1);
                    jifenjiluEntity.setJifenjiluNumber(money);
                    jifenjiluEntity.setJifenjiluName("消费 "+ shangpinEntity.getShangpinName()+" "+buyNumber+" 个 花费积分 "+jifenjiluEntity.getJifenjiluNumber());

                    shangpinOrderEntity.setShangpinOrderTruePrice(money);//实付积分
                }

            }
            shangpinOrderList.add(shangpinOrderEntity);
            jifenjiluList.add(jifenjiluEntity);
            shangpinList.add(shangpinEntity);

        }
        shangpinOrderService.insertBatch(shangpinOrderList);
        jifenjiluService.insertBatch(jifenjiluList);
        shangpinService.updateBatchById(shangpinList);
        yonghuService.updateById(yonghuEntity);
        if(cartIds != null && cartIds.size()>0)
            cartService.deleteBatchIds(cartIds);
        return R.ok();
    }











    /**
    * 退款
    */
    @RequestMapping("/refund")
    public R refund(Integer id, HttpServletRequest request){
        logger.debug("refund方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        String role = String.valueOf(request.getSession().getAttribute("role"));

        //积分记录表
        JifenjiluEntity jifenjiluEntity = new JifenjiluEntity();
        if("用户".equals(role)){
            ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
            Integer buyNumber = shangpinOrder.getBuyNumber();
            Integer shangpinOrderPaymentTypes = shangpinOrder.getShangpinOrderPaymentTypes();
            Integer shangpinId = shangpinOrder.getShangpinId();
            if(shangpinId == null)
                return R.error(511,"查不到该物品");
            ShangpinEntity shangpinEntity = shangpinService.selectById(shangpinId);
            if(shangpinEntity == null)
                return R.error(511,"查不到该物品");
            Double shangpinNewMoney = shangpinEntity.getShangpinNewMoney();
            if(shangpinNewMoney == null)
                return R.error(511,"物品价格不能为空");

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");

            // 获取折扣
            Wrapper<DictionaryEntity> dictionary = new EntityWrapper<DictionaryEntity>()
                    .eq("dic_code", "huiyuandengji_types")
                    .eq("dic_name", "会员等级类型名称")
                    .eq("code_index", yonghuEntity.getHuiyuandengjiTypes())
                    ;
            DictionaryEntity dictionaryEntity = dictionaryService.selectOne(dictionary);
            Double zhekou = 1.0;
            if(dictionaryEntity != null ){
                zhekou = Double.valueOf(dictionaryEntity.getBeizhu());
            }


            //判断是什么支付方式 1代表余额 2代表积分
            if(shangpinOrderPaymentTypes == 1){//余额支付
                //计算金额
                Double money = shangpinEntity.getShangpinNewMoney() * buyNumber  * zhekou;
                yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() + money); //设置金额
                yonghuEntity.setYonghuSumJifen(yonghuEntity.getYonghuSumJifen() - money); //设置总积分
                    if(yonghuEntity.getYonghuNewJifen() - money <0 )
                        return R.error("积分已经消费,无法退款！！！");
                yonghuEntity.setYonghuNewJifen(yonghuEntity.getYonghuNewJifen() - money); //设置现积分

                if(yonghuEntity.getYonghuSumJifen()  < 1000)
                    yonghuEntity.setHuiyuandengjiTypes(1);
                else if(yonghuEntity.getYonghuSumJifen()  < 10000)
                    yonghuEntity.setHuiyuandengjiTypes(2);
                else if(yonghuEntity.getYonghuSumJifen()  < 100000)
                    yonghuEntity.setHuiyuandengjiTypes(3);

                jifenjiluEntity.setCreateTime(new Date());
                jifenjiluEntity.setYonghuId(userId);
                jifenjiluEntity.setInsertTime(new Date());
                jifenjiluEntity.setJifenTypes(1);
                jifenjiluEntity.setJifenjiluName("退款已购买的"+ shangpinEntity.getShangpinName()+" "+buyNumber+" 个 增加的 "+money +" 积分");
                jifenjiluEntity.setJifenjiluNumber(money);
            }
            else{//积分支付

                Double money = shangpinEntity.getShangpinNewMoney() * buyNumber;
                yonghuEntity.setYonghuNewJifen(yonghuEntity.getYonghuNewJifen() + money); //设置现积分

                    jifenjiluEntity.setCreateTime(new Date());
                    jifenjiluEntity.setYonghuId(userId);
                    jifenjiluEntity.setInsertTime(new Date());
                    jifenjiluEntity.setJifenTypes(2);
                    jifenjiluEntity.setJifenjiluName("退还购买物品所消费的 "+ shangpinEntity.getShangpinName()+" "+buyNumber+" 个 花费积分 "+money);
                    jifenjiluEntity.setJifenjiluNumber(money);
            }




            shangpinOrder.setShangpinOrderTypes(2);//设置订单状态为退款
            shangpinOrderService.updateById(shangpinOrder);//根据id更新
            jifenjiluService.insert(jifenjiluEntity);
            yonghuService.updateById(yonghuEntity);
            return R.ok();
        }else{
            return R.error(511,"您没有权限退款");
        }
    }
    /**
    * 评价
    */
    @RequestMapping("/commentback")
    public R commentback(Integer id, String shangpinCommentbackContent,HttpServletRequest request){
        logger.debug("commentback方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if("用户".equals(role)){
            ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
        if(shangpinOrder == null)
            return R.error(511,"查不到该订单");
        if(shangpinOrder.getShangpinOrderTypes() != 6)
            return R.error(511,"您不能评价");
        Integer shangpinId = shangpinOrder.getShangpinId();
        if(shangpinId == null)
            return R.error(511,"查不到该物品");

        ShangpinCommentbackEntity shangpinCommentbackEntity = new ShangpinCommentbackEntity();
            shangpinCommentbackEntity.setId(id);
            shangpinCommentbackEntity.setShangpinId(shangpinId);
            shangpinCommentbackEntity.setYonghuId((Integer) request.getSession().getAttribute("userId"));
            shangpinCommentbackEntity.setShangpinCommentbackContent(shangpinCommentbackContent);
            shangpinCommentbackEntity.setReplyContent(null);
            shangpinCommentbackEntity.setInsertTime(new Date());
            shangpinCommentbackEntity.setUpdateTime(null);
            shangpinCommentbackEntity.setCreateTime(new Date());
            shangpinCommentbackService.insert(shangpinCommentbackEntity);

            shangpinOrder.setShangpinOrderTypes(1);//设置订单状态为已评价
            shangpinOrderService.updateById(shangpinOrder);//根据id更新
            return R.ok();
        }else{
            return R.error(511,"您没有权限评价");
        }
    }



    /**
     * 收货
     */
    @RequestMapping("/receiving")
    public R receiving(Integer id){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        ShangpinOrderEntity  shangpinOrderEntity = new  ShangpinOrderEntity();
        shangpinOrderEntity.setId(id);
        shangpinOrderEntity.setShangpinOrderTypes(6);
        boolean b =  shangpinOrderService.updateById( shangpinOrderEntity);
        if(!b){
            return R.error("收货出错");
        }
        return R.ok();
    }



    /**
    * 支付(没有多商家和积分记录操作，要改，因为之前没有未支付这种状态，所以没有加)
    */
    @RequestMapping("/pay")
    public R pay(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request){
        logger.debug("pay方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder);
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if("用户".equals(role)){
                                shangpinOrder = shangpinOrderService.selectById(shangpinOrder.getId());
            if(shangpinOrder == null)
                 return R.error(511,"查询不到该订单");
                                ShangpinEntity shangpinEntity = shangpinService.selectById(shangpinOrder.getShangpinId());
            if(shangpinEntity == null)
                return R.error(511,"查不到该物品");
            Double shangpinNewMoney = shangpinEntity.getShangpinNewMoney();
            if(shangpinNewMoney == null)
                return R.error(511,"物品价格不能为空");

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");
            double balance = yonghuEntity.getNewMoney() - shangpinEntity.getShangpinNewMoney();//余额
            if(balance<0)
                return R.error(511,"余额不够支付");

                                shangpinOrder.setShangpinOrderTypes(3); //设置订单状态为已支付
                                shangpinOrder.setCreateTime(new Date());
                                shangpinOrder.setInsertTime(new Date());
                                shangpinOrderService.updateById(shangpinOrder);//根据id更新
            yonghuEntity.setNewMoney(balance);
            yonghuService.updateById(yonghuEntity);
            return R.ok();
        }else{
            return R.error(511,"您没有权限支付订单");
        }
    }


















}

