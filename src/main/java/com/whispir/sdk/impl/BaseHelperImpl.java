package com.whispir.sdk.impl;

import com.whispir.sdk.WhispirSDK;

public abstract class BaseHelperImpl {
	protected WhispirSDK sdk;
	
	public BaseHelperImpl(WhispirSDK sdk) {
		this.sdk = sdk;
	}
}
