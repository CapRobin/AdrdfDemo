package com.adrdf.base.view.wheel;

/**
 * Copyright © CapRobin
 *
 * Name：RdfNumericWheelAdapter
 * Describe：轮子适配器（数字）
 * Date：2017-06-17 19:28:57
 * Author: CapRobin@yeah.net
 *
 */
public class RdfNumericWheelAdapter implements RdfWheelAdapter {

	/** 数字的最大值. */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** 数字最小值. */
	private static final int DEFAULT_MIN_VALUE = 0;

	/** 选择范围 小值. */
	private int minValue;

	/** 选择范围 大值. */
	private int maxValue;

	/** The format. */
	private String format;

	/**
	 * 用0-9构造.
	 */
	public RdfNumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * 构造函数.
	 *
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public RdfNumericWheelAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, null);
	}

	/**
	 * 构造函数.
	 *
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format the format string
	 */
	public RdfNumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}


	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}


	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}
