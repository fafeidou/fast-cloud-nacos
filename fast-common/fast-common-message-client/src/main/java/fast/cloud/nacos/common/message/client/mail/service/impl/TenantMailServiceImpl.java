package fast.cloud.nacos.common.message.client.mail.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fast.cloud.nacos.common.message.client.constants.MessageDestinationInfo;
import fast.cloud.nacos.common.message.client.mail.model.TenantMailModel;
import fast.cloud.nacos.common.message.client.mail.service.TenantMailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class TenantMailServiceImpl implements TenantMailService {

    private final String simpleMailDestination = MessageDestinationInfo.MessageTopic.MAIL_TOPIC + ":" + MessageDestinationInfo.MessageTag.MAIL_SIMPLE;
    private final String htmlMailDestination = MessageDestinationInfo.MessageTopic.MAIL_TOPIC + ":" + MessageDestinationInfo.MessageTag.MAIL_HTML;
    @Autowired(required = false)
    protected RocketMQTemplate rocketMQTemplate;


    @Override
    public void sendSimpleTextMail(TenantMailModel tenantMailModel) {
        this.sendOne(simpleMailDestination, tenantMailModel);
    }

    @Override
    public void sendHtmlMail(TenantMailModel tenantMailModel) {
        this.sendOne(htmlMailDestination, tenantMailModel);
    }

    @Override
    public void sendSimpleTextMailList(List<TenantMailModel> tenantMailModels) {
        this.sendList(simpleMailDestination, tenantMailModels);
    }

    @Override
    public void sendHtmlMailList(List<TenantMailModel> tenantMailModels) {
        this.sendList(htmlMailDestination, tenantMailModels);
    }


    private void sendOne(String topic, TenantMailModel tenantMailModel) {
        if (Objects.isNull(tenantMailModel)) {
            return;
        }
        this.sendList(topic, Lists.newArrayList(tenantMailModel));
    }

    private void sendList(String topic, List<TenantMailModel> tenantMailModels) {
        String key = "";
        if (!CollectionUtils.isEmpty(tenantMailModels)) {
            key = tenantMailModels.get(0).getMsgKey();
        }
        Map<String, Object> headers = setMsgKey(key);
        rocketMQTemplate.convertAndSend(topic, tenantMailModels, headers);
    }


    private Map<String, Object> setMsgKey(String msgKey) {
        Map<String, Object> headers = Maps.newHashMap();
        String rocketMqMsgKey;
        if (StringUtils.isBlank(msgKey)) {
            rocketMqMsgKey = UUID.randomUUID().toString();
        } else {
            rocketMqMsgKey = msgKey;
        }
        headers.put(MessageConst.PROPERTY_KEYS, rocketMqMsgKey);
        return headers;
    }

}
