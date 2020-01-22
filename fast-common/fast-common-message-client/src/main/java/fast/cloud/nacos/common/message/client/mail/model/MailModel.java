package fast.cloud.nacos.common.message.client.mail.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件需要设置的参数
 *
 * @description 发送邮件需要的参数配置
 */
@Data
@NoArgsConstructor
public class MailModel implements Serializable {

    private static final long serialVersionUID = 863572348682453853L;

    /**
     * 发件人 @see ${spring.mail.username}
     */
    private String sender;
    /**
     * 单个收件人列表
     */
    private String sendToOne;
    /**
     * 单个抄送
     */
    private String ccOne;

    /**
     * 单个密送
     */
    private String bccOne;
    /**
     * 多个接收对象
     */
    private List<String> sendToList;
    /**
     * 多个抄送对象
     */
    private List<String> ccList;
    /**
     * 多个密送对象
     */
    private List<String> bccList;
    /**
     * 主题
     */
    private String subject;
    /**
     * 文本格式的文章 或者是应该用到的模板
     */
    private String content;
    /**
     * 是不是文本形式
     */
    private boolean html = true;
    /**
     * 附件文件，需要有文件名
     */
    private List<File> attachments;
    /**
     * 附件文件，url形式
     */
    private List<String> urlAttachment;
    /**
     * 附件文件，文件名和url形式
     */
    private Map<String, String> attachWithNameAndUrl;
    /**
     * 静态资源文件，需要有文件名
     */
    private List<File> resources;
    /**
     * 模板参数
     */
    private Map<String, Object> params;
    /**
     * 待替换的模板
     */
    private String template;
    /**
     * 发件人姓名
     */
    private String fromName;
    /**
     * 设置回复人
     */
    private List<String> replyTos;
    /**
     * 设置回复人
     */
    private Date sendDate;

    //未开发字段
    private String disposition;
    private String contentID;
    private String contentMD5;
    private String description;
    private List<String> contentLanguages;
    private Map<String, String> headerMap;

    /**
     * 设置一个收件人所需要的参数
     *
     * @param sendToOne 设置一个收件人
     * @param subject   设置主题
     * @param content   设置内容
     */
    public MailModel(String sendToOne, String subject, String content) {
        this.sendToOne = sendToOne;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 设置多个收件人需要发送的参数
     *
     * @param sendToList 设置多个收件人
     * @param subject    设置主题
     * @param content    设置内容
     */
    public MailModel(List<String> sendToList, String subject, String content) {
        this.sendToList = sendToList;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 设置附件
     *
     * @param sendToList  设置收件人
     * @param subject     设置主题
     * @param content     设置内容
     * @param attachments 设置附件
     */
    public MailModel(List<String> sendToList, String subject, String content, List<File> attachments) {
        this.sendToList = sendToList;
        this.subject = subject;
        this.content = content;
        this.attachments = attachments;
    }

    /**
     * 设置模板发送内容
     *
     * @param sendToList 收件人
     * @param subject    主题
     * @param template   模板
     * @param params     模板参数
     */
    public MailModel(List<String> sendToList, String subject, String template, Map<String, Object> params) {
        this.sendToList = sendToList;
        this.subject = subject;
        this.template = template;
        this.params = params;
    }

    /**
     * 设置sender
     *
     * @param sender 发件人
     * @return 返回MailModel
     */
    public MailModel sender(String sender) {
        this.setSender(sender);
        return this;
    }

    /**
     * 设置一个收件人
     *
     * @param sendToOne 一个收件人
     * @return 返回MailModel
     */
    public MailModel toOne(String sendToOne) {
        this.setSendToOne(sendToOne);
        return this;
    }

    /**
     * 设置抄送人
     *
     * @param ccOne 抄送人
     * @return 返回MailModel
     */
    public MailModel ccOne(String ccOne) {
        this.setCcOne(ccOne);
        return this;
    }

    /**
     * 设置密送人
     *
     * @param bccOne 密送人
     * @return 返回MailModel
     */
    public MailModel bccOne(String bccOne) {
        this.setBccOne(bccOne);
        return this;
    }

    /**
     * 设置多个收件人
     *
     * @param sendToList 多个收件人
     * @return 返回MailModel
     */
    public MailModel toList(List<String> sendToList) {
        this.setSendToOne("");
        this.setSendToList(sendToList);
        return this;
    }

    /**
     * 设置多个抄送人
     *
     * @param ccList 多个抄送人
     * @return 返回MailModel
     */
    public MailModel ccList(List<String> ccList) {
        this.setCcOne("");
        this.setCcList(ccList);
        return this;
    }

    /**
     * 设置多个密送人
     *
     * @param bccList 多个密送人
     * @return 返回MailModel
     */
    public MailModel bccList(List<String> bccList) {
        this.setBccOne("");
        this.setBccList(bccList);
        return this;
    }

    /**
     * 设置主题
     *
     * @param subject 主题
     * @return 返回MailModel
     */
    public MailModel subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     * @return 返回MailModel
     */
    public MailModel content(String content) {
        this.setContent(content);
        return this;
    }

    /**
     * 设置附件
     *
     * @param attachments 附件列表
     * @return 返回MailModel
     */
    public MailModel attachments(List<File> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    /**
     * 设置静态资源内容
     *
     * @param resources 静态资源内容
     * @return 返回MailModel
     */
    public MailModel resources(List<File> resources) {
        this.setResources(resources);
        return this;
    }

    /**
     * 设置模板参数
     *
     * @param params 模板参数
     * @return 返回MailModel
     */
    public MailModel params(Map<String, Object> params) {
        this.setParams(params);
        return this;
    }

    /**
     * 设置内容是html形式
     *
     * @return 返回MailModel
     */
    public MailModel html() {
        this.setHtml(true);
        return this;
    }

    /**
     * 设置内容是text形式
     *
     * @return 返回MailModel
     */
    public MailModel text() {
        this.setHtml(false);
        return this;
    }

    /**
     * 设置字符串模板
     *
     * @param template 字符串模板
     * @return 返回MailModel
     */
    public MailModel template(String template) {
        this.setTemplate(template);
        return this;
    }

    /**
     * 设置发件人姓名
     *
     * @param fromName 发件人姓名
     * @return 返回MailModel
     */
    public MailModel fromName(String fromName) {
        this.setFromName(fromName);
        return this;
    }

    /**
     * 设置回复人
     *
     * @param replyTos 回复人
     * @return 返回MailModel
     */
    public MailModel replyTos(List<String> replyTos) {
        this.setReplyTos(replyTos);
        return this;
    }

    /**
     * 设置发送时间
     *
     * @param sendDate 发送时间
     * @return 返回MailModel
     */
    public MailModel sendDate(Date sendDate) {
        this.setSendDate(sendDate);
        return this;
    }

    /**
     * 设置url形式的附件，自动下载
     *
     * @param urls url形式的附件
     * @return
     */
    public MailModel urlAttachment(List<String> urls) {
        this.setUrlAttachment(urls);
        return this;
    }

    public MailModel attachWithNameAndUrl(Map<String, String> nameAndUrl) {
        this.setAttachWithNameAndUrl(nameAndUrl);
        return this;
    }
}
