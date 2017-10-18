package de.mw.mwdata.app.admin.angweb3.control;

//@XmlRootElement(name = "player")
public class Message {

	String name;
	String text;

	public Message() {

	}

	public Message(String name, String text) {
		this.name = name;
		this.text = text;
	}

	// @XmlElement
	public String getName() {
		return name;
	}

	// @XmlElement
	public String getText() {
		return text;
	}

}
