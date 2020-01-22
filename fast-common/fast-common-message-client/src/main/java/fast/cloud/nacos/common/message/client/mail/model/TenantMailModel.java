package fast.cloud.nacos.common.message.client.mail.model;

import fast.cloud.nacos.common.tenant.context.TenantStore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import java.io.File;
import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public class TenantMailModel extends MailModel {

    private String tenantId;
    private String msgKey;
    private Long fromUserId;
    private Long toUserId;

    private String toUid;
    /**
     * 业务的模板表中的code
     */
    private String code;

    public TenantMailModel() {
        setTenantId(TenantStore.getTenantId());
    }

    public TenantMailModel(String sendToOne, String subject, String content) {
        setSendToOne(sendToOne);
        setSubject(subject);
        setContent(content);
        setTenantId(TenantStore.getTenantId());
    }

    /**
     * 设置多个收件人需要发送的参数
     *
     * @param sendToList 设置多个收件人
     * @param subject    设置主题
     * @param content    设置内容
     */
    public TenantMailModel(List<String> sendToList, String subject, String content) {
        setSendToList(sendToList);
        setSubject(subject);
        setContent(content);
        setTenantId(TenantStore.getTenantId());
    }

    /**
     * 设置附件
     *
     * @param sendToList  设置收件人
     * @param subject     设置主题
     * @param content     设置内容
     * @param attachments 设置附件
     */
    public TenantMailModel(List<String> sendToList, String subject, String content, List<File> attachments) {
        setSendToList(sendToList);
        setSubject(subject);
        setContent(content);
        setAttachments(attachments);
        setTenantId(TenantStore.getTenantId());
    }

    /**
     * 设置模板发送内容
     *
     * @param sendToList 收件人
     * @param subject    主题
     * @param template   模板
     * @param params     模板参数
     */
    public TenantMailModel(List<String> sendToList, String subject, String template, Map<String, Object> params) {
        setSendToList(sendToList);
        setSubject(subject);
        setTemplate(template);
        setParams(params);
        setTenantId(TenantStore.getTenantId());
    }

    @Override
    public void setParams(@NonNull Map<String, Object> params) {
        if (this.getParams() != null && this.getParams().size() != 0) {
            this.getParams().putAll(params);
        } else {
            super.setParams(params);
        }
    }

    @Override
    public MailModel params(Map<String, Object> params) {
        this.setParams(params);
        return this;
    }


}

