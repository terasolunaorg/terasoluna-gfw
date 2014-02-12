/*
 * Copyright (C) 2013 terasoluna.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.web.message;

import java.lang.reflect.Array;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageType;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.util.HtmlEscapeUtils;

/**
 * Tag for displaying messages in panel format in JSP page<br>
 * <p>
 * Creates display tag, converts the object which is set in {@code pageContext}
 * to String and sets this string inside the tag <br>
 * This tag displays the contents of
 * {@code org.terasoluna.gfw.common.message.ResultMessages} by default. However,
 * {@code String} or {@code List<string>} set in {@code Model} can also be
 * displayed using this tag
 * </p>
 * <p>
 * <b>when messagesPanel is set to use {@code ResultMessages} (default)</b>
 * </p>
 * <p>
 * By default, configuration elements of {@code messagesPanel} are set to
 * following values<br>
 * {@link #panelElement} is "div"<br>
 * {@link #panelClassName} is "alert"<br>
 * {@link #panelTypeClassPrefix} is "alert-"<br>
 * {@link #messagesType} is "null"<br>
 * {@link #outerElement} is "ul"<br>
 * {@link #innerElement} is "li"<br>
 * <br>
 * All the above elements can be given non-default values.
 * </p>
 * <p>
 * Example<br>
 * 
 * <pre>
 * &lt;t:messagesPanel /&gt;<br>
 * </pre>
 * 
 * </p>
 * <p>
 * Resulting Output will be<br>
 * 
 * <pre>
 * &lt;div class=&quot;alert alert-success&quot;&gt;&lt;ul&gt;&lt;li&gt;Hello World!!&lt;/li&gt;&lt;/ul&gt;&lt;/div&gt;<br>
 * </pre>
 * 
 * </p>
 * <p>
 * By default, a combination of, ``alert`` and any one of
 * ``alert-success,alert-info,alert-warn,alert-error`` classes of bootstrap, is
 * used in ``messagesPanel`` <br>
 * the suffix after ``alert-`` is specified using ``type`` of ResultMessages
 * </p>
 * <p>
 * <b>When the message is in {@code String} and {@code messagesPanel} is using
 * default values<br>
 * </b>
 * </p>
 * <p>
 * Configuration example:<br>
 * 
 * <pre>
 * &lt;t:messagesPanel messagesAttributeName=&quot;textMessage&quot; /&gt;<br>
 * </pre>
 * 
 * </p>
 * <p>
 * Result:<br>
 * 
 * <pre>
 * &lt;div class=&quot;alert&quot;&gt;&lt;ul&gt;&lt;li&gt;textMessage&lt;/li&gt;&lt;/ul&gt;&lt;/div&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * <b>When the message is in {@code String} and {@code messagesPanel} is using
 * non-default values<br>
 * </b>
 * </p>
 * <p>
 * This is the case of not using ResultMessages:
 * </p>
 * 
 * <pre>
 * <div class="row"> <div class="span12"> <h2>Message pattern</h2> <div class="alert alert-success">
 * </pre>
 * <p>
 * Configuration example:<br>
 * 
 * <pre>
 * &lt;t:messagesPanel messagesAttributeName=&quot;textMessage&quot; panelClassName=&quot;notice&quot;
 * panelTypeClassPrefix=&quot;&quot; /&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * Result:<br>
 * 
 * <pre>
 * &lt;div class=&quot;notice&quot;&gt;&lt;ul&gt;&lt;li&gt;textMessage&lt;/li&gt;&lt;/ul&gt;&lt;/div&gt;
 * </pre>
 * 
 * </p>
 */
public class MessagesPanelTag extends RequestContextAwareTag {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * default panel class name.
	 */
	protected static final String DEFAULT_PANEL_CLASS_NAME = "alert";

	/**
	 * default panel type class prefix.
	 */
	protected static final String DEFAULT_PANEL_TYPE_CLASS_PREFIX = "alert-";

	/**
	 * default panel element.
	 */
	protected static final String DEFAULT_PANEL_ELEMENT = "div";

	/**
	 * default outer element.
	 */
	protected static final String DEFAULT_OUTER_ELEMENT = "ul";

	/**
	 * default inner element.
	 */
	protected static final String DEFAULT_INNER_ELEMENT = "li";

	/**
	 * messages attribute name.
	 */
	private String messagesAttributeName = ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;

	/**
	 * panel class name.
	 */
	private String panelClassName = DEFAULT_PANEL_CLASS_NAME;

	/**
	 * panel type class prefix.
	 */
	private String panelTypeClassPrefix = DEFAULT_PANEL_TYPE_CLASS_PREFIX;

	/**
	 * panel element.
	 */
	private String panelElement = DEFAULT_PANEL_ELEMENT;

	/**
	 * outer element.
	 */
	private String outerElement = DEFAULT_OUTER_ELEMENT;

	/**
	 * inner element.
	 */
	private String innerElement = DEFAULT_INNER_ELEMENT;

	/**
	 * messages type.
	 */
	private String messagesType = null;
	
	/**
	 * Flag to indicate whether html escaping is to be disabled or not
	 */
	private Boolean disableHtmlEscape = Boolean.FALSE;
	
	/**
	 * Creates TagWriter
	 * 
	 * @return Created TagWriter
	 */
	TagWriter createTagWriter() {
		TagWriter tagWriter = new TagWriter(this.pageContext);
		return tagWriter;
	}

	/**
	 * Creates messagesPanel tag
	 * 
	 * @throws JspException
	 *             In case when {@link JspException} is generated later in the
	 *             chain when tag configured by MessagesPanel could not be
	 *             created
	 * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
	 */
	@Override
	protected int doStartTagInternal() throws JspException {

		if (!StringUtils.hasText(this.panelElement)
				&& !StringUtils.hasText(this.outerElement)
				&& !StringUtils.hasText(this.innerElement)) {
			throw new JspTagException(
					"At least one out of panelElement, outerElement, innerElement should be set");
		}

		TagWriter tagWriter = createTagWriter();

		Object messages = this.pageContext.findAttribute(messagesAttributeName);

		if (messages != null) {

			if (StringUtils.hasText(panelElement)) {
				tagWriter.startTag(panelElement); // <div>

				StringBuilder className = new StringBuilder(panelClassName);
				String type = getType(messages);

				if (panelTypeClassPrefix != null && StringUtils.hasText(type)) {

					if (StringUtils.hasLength(className)) {
						className.append(" ");
					}
					className.append(panelTypeClassPrefix);
				}
				className.append(type);

				if (StringUtils.hasText(className)) {
					tagWriter.writeAttribute("class", className.toString());
				}
			}

			{
				if (StringUtils.hasText(outerElement)) {
					tagWriter.startTag(outerElement); // <ul>
				}

				writeMessages(tagWriter, messages);

				if (StringUtils.hasText(outerElement)) {
					tagWriter.endTag(); // </ul>
				}
			}

			if (StringUtils.hasText(panelElement)) {
				tagWriter.endTag(); // </div>
			}
		}

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Writes the messages which have been set in the model
	 * <p>
	 * If messages stored in the model is in the form of a class that extends
	 * {@code Iterable} or an Array, {@link #writeMessage(TagWriter, Object)} is
	 * called multiple times, to write<br>
	 * {@link #innerElement} and messages.<br>
	 * 
	 * If there is only a single message, this method calls
	 * {@link #writeMessage(TagWriter, Object)} only once
	 * </p>
	 * 
	 * @param tagWriter
	 * @param message
	 * @throws JspException
	 *             If {@link JspException} occurs in caller writeMessage
	 */
	protected void writeMessages(TagWriter tagWriter, Object messages)
			throws JspException {
		Class<?> clazz = messages.getClass();
		if (Iterable.class.isAssignableFrom(clazz)) {
			Iterable<?> col = (Iterable<?>) messages;
			for (Object message : col) {
				writeMessage(tagWriter, message);
			}
		} else if (clazz.isArray()) {
			Class<?> type = clazz.getComponentType();
			if (Object.class.isAssignableFrom(type)) {
				Object[] arr = (Object[]) messages;
				for (Object message : arr) {
					writeMessage(tagWriter, message);
				}
			} else {
				int len = Array.getLength(messages);
				for (int i = 0; i < len; i++) {
					Object message = Array.get(messages, i);
					writeMessage(tagWriter, message);
				}
			}
		} else {
			writeMessage(tagWriter, messages);
		}
	}

	/**
	 * writes the message tag.
	 * <p>
	 * If {@link #innerElement} is specified, the tag specified in it will be
	 * applied around the message.<br>
	 * </p>
	 * 
	 * @param tagWriter
	 * @param message
	 * @throws JspException
	 *             Occurs when {@link #JspTagException} occurs in case when
	 *             nothing is set in the configuration of the tag that
	 *             configures MessagePanel using tagWriter.
	 */
	protected void writeMessage(TagWriter tagWriter, Object message)
			throws JspException {
		if (message != null) {
			if (StringUtils.hasText(innerElement)) {
				tagWriter.startTag(innerElement); // <li>
			}
			if(disableHtmlEscape) {
			    tagWriter.appendValue(getText(message));
			} else {
			    tagWriter.appendValue(HtmlEscapeUtils.htmlEscape(getText(message)));
			}

			if (StringUtils.hasText(innerElement)) {
				tagWriter.endTag(); // </li>
			}
		}
	}

	/**
	 * Converts the Object received as parameter to string and returns it.
	 * <p>
	 * Returns the String as it is<br>
	 * {@code ResultMessage} is returned after conversion using
	 * {@link #getText(ResultMessage resultMessage)}. {@code Throwable} is
	 * returned after text conversion.<br>
	 * While all other instances of Object are returned after conversion using
	 * {@link #getTextInOtherCase}.
	 * </p>
	 * 
	 * @param message
	 *            Object
	 * @return Message after conversion to String
	 */
	private String getText(Object message) {
		String text = null;

		if (message instanceof String) {
			text = (String) message;
		} else if (message instanceof ResultMessage) {
			ResultMessage resultMessage = (ResultMessage) message;
			text = getText(resultMessage);
		} else if (message instanceof Throwable) {
			Throwable throwable = (Throwable) message;
			text = throwable.getMessage();
		} else {
			text = getTextInOtherCase(message);
		}
		return text;
	}

	/**
	 * Returns the String after the code set in {@code ResultMessage} undergoes
	 * messageSource conversion in line with the locale.
	 * <p>
	 * </p>
	 * 
	 * @param resultMessage
	 *            ResultMessage
	 */
	private String getText(ResultMessage resultMessage) {
		Locale locale = getRequestContext().getLocale();
		MessageSource messageSource = getRequestContext().getMessageSource();
		String text = ResultMessageUtils.resolveMessage(resultMessage,
				messageSource, locale);
		return text;
	}

	/**
	 * converts the parameter to String and returns it.
	 * <p>
	 * </p>
	 * 
	 * @param message
	 *            Object
	 */
	protected String getTextInOtherCase(Object message) {
		return message.toString();
	}

	/**
	 * Returns the type of Message
	 * <p>
	 * If {@link #messagesType} is has been set (contains value), then return
	 * {@link #messagesType} as it is.<br>
	 * If the data-type of Object passed as parameter is {@code ResultMessages},
	 * then fetches the type set inside {@code ResultMessages} and returns it. <br>
	 * If {@link #messagesType} is has not been set (doesn't contains value) and
	 * data-type of passed parameter is also not {@code ResultMessages}, then
	 * return empty string.
	 * </p>
	 * 
	 * @param messages
	 *            Object fetched from PageContext using the value of
	 *            ``messagesAttributeName`` property as attribute name.
	 */
	private String getType(Object messages) {
		String type = "";
		if (messagesType != null) {
			type = messagesType;
		} else if (messages instanceof ResultMessages) {
			ResultMessages resultMessages = (ResultMessages) messages;
			ResultMessageType iResultMessageType = resultMessages.getType();
			type = iResultMessageType.getType();
		}
		return type;
	}

	/**
	 * Set the name of the model that stores the messages.
	 * <p>
	 * </p>
	 * 
	 * @param messagesAttributeName
	 *            Attribute name that is used to store messages
	 */
	public void setMessagesAttributeName(String messagesAttributeName) {
		this.messagesAttributeName = messagesAttributeName;
	}

	/**
	 * Set the class that will configure the MessagesPanel tag. <br>
	 * CSS class must be set. <br>
	 * If this item is being set, it is assumed that
	 * {@link #panelTypeClassPrefix} must be empty.
	 * <p>
	 * </p>
	 * 
	 * @param panelClassName
	 *            CSS class
	 */
	public void setPanelClassName(String panelClassName) {
		this.panelClassName = panelClassName;
	}

	/**
	 * Sets the prefix of the class name that configures the messagesPanel tag
	 * <p>
	 * [Caution Notes]<br>
	 * {@link #messagesType} gets attached at the end. Hence the structure of
	 * class must be <br>
	 * {@link #panelTypeClassPrefix} + {@link #messagesType}.
	 * </p>
	 * 
	 * @param panelTypeClassPrefix
	 *            Prefix of CSS class
	 */
	public void setPanelTypeClassPrefix(String panelTypeClassPrefix) {
		this.panelTypeClassPrefix = panelTypeClassPrefix;
	}

	/**
	 * Sets the tag that configures MessagesPanel
	 * <p>
	 * Points to be careful:<br>
	 * Only the tag name should be set. "<>" used while using the tag in HTML
	 * should not be included. For ex: "div"<br>
	 * Specify the tag which have class property.
	 * </p>
	 * 
	 * @param panelElement
	 *            html tag
	 */
	public void setPanelElement(String panelElement) {
		this.panelElement = panelElement;
	}

	/**
	 * Sets the outer tag which configures MessagesPanel tag
	 * <p>
	 * Points to be careful:<br>
	 * Only the tag name should be set. "<>" used while using the tag in HTML
	 * should not be included. For ex: "span"<br>
	 * </p>
	 * 
	 * @param outerElement
	 *            html tag
	 */
	public void setOuterElement(String outerElement) {
		this.outerElement = outerElement;
	}

	/**
	 * Sets the inner tag which configures MessagesPanel tag
	 * <p>
	 * Points to be careful:<br>
	 * Only the tag name should be set. "<>" used while using the tag in HTML
	 * should not be included. For ex: "span"<br>
	 * </p>
	 * 
	 * @param innerElement
	 *            html tag
	 */
	public void setInnerElement(String innerElement) {
		this.innerElement = innerElement;
	}

	/**
	 * Specify the type of the message which messagesPanel will display
	 * <p>
	 * Points to be careful:<br>
	 * Should be used only when specifying {@code String} and not
	 * {@code ResultMessages}<br>
	 * When {@code ResultMessages} is being used, {@link #messagesType} will be
	 * fetched from ResultMessages<br>
	 * This method assumes that if {@link #messagesType} is being specified,
	 * BootStrap CSS is used<br>
	 * </p>
	 * 
	 * @param messagesType
	 *            type of message
	 */
	public void setMessagesType(String messagesType) {
		this.messagesType = messagesType;
	}
	
	/**
	 * Sets the value for HtmlEscapingEnabled property.
	 * <p>
	 * IF set to true, html escaping is enabled. 
	 * Default value is true.
	 * 
	 * @param disableHtmlEscape
	 *            value of disableHtmlEscape
	 */
	public void setDisableHtmlEscape(String disableHtmlEscape) {
		if(StringUtils.hasText(disableHtmlEscape)) {
			if(disableHtmlEscape.equalsIgnoreCase("true") || 
			        disableHtmlEscape.equalsIgnoreCase("false")) {
				this.disableHtmlEscape = Boolean.valueOf(disableHtmlEscape);
			}
		}
	}
}
