package cn.micro.biz.entity.order;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.goods.DeliveryAddress;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("order")
public class Order extends MicroEntity<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * Member id
     * <p>
     * {@link Member#id}
     **/
    private Long memberId;
    /**
     * Delivery address id
     * <p>
     * {@link DeliveryAddress#id}
     **/
    private Long addressId;
    /**
     * Order logistics id
     * <p>
     * {@link OrderLogistics#id}
     **/
    private Long logisticsId;
    /**
     * Order invoice id
     * <p>
     * {@link OrderInvoice#id}
     **/
    private Long invoiceId;

    /**
     * Goods count
     **/
    private Integer goodsCount;
    /**
     * Goods amount total
     **/
    private BigDecimal goodsAmountTotal;

    /**
     * Order status(未付款,已付款,已发货,已签收,退货申请,退货中,已退货,取消交易)
     **/
    private Integer orderStatus;
    /**
     * 订单金额(实际付款金额)
     **/
    private BigDecimal orderAmountTotal;
    /**
     * 运费金额
     **/
    private BigDecimal logisticsFee;
    /**
     * 是否开票（是否开具发票）
     **/
    private Integer invoice;

    /**
     * 订单支付渠道
     **/
    private Integer payChannel;
    /**
     * 订单单号（order_no, 唯一值，供客户查询）
     **/
    private String orderNo;
    /**
     * 订单支付单号 (第三方支付流水号)
     **/
    private String outTradeNo;
    /**
     * 付款时间
     **/
    private Date payTime;
    /**
     * 发货时间
     **/
    private Date deliveryTime;
    /**
     * 客户备注
     **/
    private String remark;

}
