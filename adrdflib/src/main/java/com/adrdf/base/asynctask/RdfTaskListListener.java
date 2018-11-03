/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adrdf.base.asynctask;

import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskListListener
 * Describe：数据监听器
 * Date：2017-02-27 15:36:33
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfTaskListListener extends RdfTaskListener {

	/**
	 * 执行开始.
	 * 
	 * @return 返回的结果列表
	 */
	public abstract List<?> getList();

	/**
	 * 执行完成后回调. 不管成功与否都会执行
	 * 
	 * @param paramList 返回的List
	 */
	public abstract void update(List<?> paramList);

}
