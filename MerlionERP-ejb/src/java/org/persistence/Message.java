package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "MESSAGE")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long message_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date msg_date_time;
	
	@Basic
	private String subject;
	
	@Basic
	private String content;
	
	@Basic
	private String sender;
	
	@Basic
	private String receiver;
	
	public void setMessage_id(Long param) {
		this.message_id = param;
	}

	public Long getMessage_id() {
		return message_id;
	}
	
	public void setMsg_date_time(Date param) {
		this.msg_date_time = param;
	}

	public Date getMsg_date_time() {
		return msg_date_time;
	}

	public void setSubject(String param) {
		this.subject = param;
	}

	public String getSubject() {
		return subject;
	}

	public void setContent(String param) {
		this.content = param;
	}

	public String getContent() {
		return content;
	}

	public void setSender(String param) {
		this.sender = param;
	}

	public String getSender() {
		return sender;
	}

	public void setReceiver(String param) {
		this.receiver = param;
	}

	public String getReceiver() {
		return receiver;
	}

}