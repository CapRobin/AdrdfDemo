package com.adrdf.base.app.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright © CapRobin
 *
 * Name：RdfSampleItem
 * Describe：TAB选项条目实体
 * Date：2017-03-28 09:48:43
 * Author: CapRobin@yeah.net
 *
 */
public class RdfSampleItem implements Parcelable,Comparable<RdfSampleItem>  {

	/** 菜单的id. */
	private String id;

	/** 菜单的图标. */
	private String icon;

	/** 菜单的文本. */
	private String text;

	/** 菜单的首字. */
	private String firstLetter;

	/** 菜单的值. */
	private String value;

	/**
	 * 空构造函数.
	 */
	public RdfSampleItem() {
		super();
	}

	/**
	 * 构造函数.
	 * @param id the id
	 * @param text the text
	 */
	public RdfSampleItem(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

    /**
     * 构造函数
     * @param id
     * @param text
     * @param firstLetter
     */
	public RdfSampleItem(String id, String text, String firstLetter) {
		super();
		this.id = id;
		this.text = text;
		this.firstLetter = firstLetter;
	}

    /**
     * 构造函数.
     * @param id
     * @param text
     * @param firstLetter
     * @param value
     */
    public RdfSampleItem(String id, String text, String firstLetter, String value) {
        super();
        this.id = id;
        this.text = text;
        this.firstLetter = firstLetter;
        this.value = value;
    }

	/**
	 * 构造函数.
	 * @param item
	 */
	public RdfSampleItem(RdfSampleItem item){
		id = item.getId();
		icon = item.getIcon();
		text = item.getText();
		firstLetter = item.getFirstLetter();
		value = item.getValue();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public RdfSampleItem(Parcel parcel){
		id = parcel.readString();
		icon = parcel.readString();
		text = parcel.readString();
		firstLetter = parcel.readString();
		value = parcel.readString();
	}


    public static final Parcelable.Creator<RdfSampleItem> CREATOR = new Creator<RdfSampleItem>(){
        @Override
        public RdfSampleItem[] newArray(int size) {
            return new RdfSampleItem[size];
        }

        @Override
        public RdfSampleItem createFromParcel(Parcel parcel){
            return new RdfSampleItem(parcel);
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
		parcel.writeString(icon);
        parcel.writeString(text);
        parcel.writeString(firstLetter);
        parcel.writeString(value);
    }

	@Override
	public int compareTo(RdfSampleItem another) {
		if (this.getFirstLetter().equals("@")
				|| another.getFirstLetter().equals("#")) {
			return -1;
		} else if (this.getFirstLetter().equals("#")
				|| another.getFirstLetter().equals("@")) {
			return 1;
		} else {
			return this.getFirstLetter().compareTo(another.getFirstLetter());
		}
	}
}

