package fast.cloud.nacos.common.message.client.mail.service;

import fast.cloud.nacos.common.message.client.mail.model.TenantMailModel;

import java.util.List;

/**
 * @description 多租户邮件发送处理
 */
public interface TenantMailService {

    /**
     * 发送单个简单文本邮件，不带附件
     *
     * @param tenantMailModel
     */
    void sendSimpleTextMail(TenantMailModel tenantMailModel);

    /**
     * 发送多个富文本邮件，带附件和模板自动渲染功能
     *
     * @param tenantMailModel
     * @return
     */
    void sendHtmlMail(TenantMailModel tenantMailModel);


    /**
     * (建议使用TenantMailModel中的toList属性来发送给多个邮件接受者)发送多个简单文本邮件，不带附件
     *
     * @param tenantMailModels
     */
    void sendSimpleTextMailList(List<TenantMailModel> tenantMailModels);

    /**
     * (建议使用TenantMailModel中的toList属性来发送给多个邮件接受者)发送多个富文本邮件，带附件和模板自动渲染功能
     *
     * @param tenantMailModels
     */
    void sendHtmlMailList(List<TenantMailModel> tenantMailModels);


}
