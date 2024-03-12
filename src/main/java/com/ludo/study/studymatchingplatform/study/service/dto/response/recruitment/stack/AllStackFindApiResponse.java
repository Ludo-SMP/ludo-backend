package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.stack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllStackFindApiResponse {

	@JsonProperty("ok")
	private final boolean ok;

	@JsonProperty("message")
	private final String message;

	@JsonProperty(value = "data")
	private final StackResponses stackResponses;

	public AllStackFindApiResponse(boolean ok, String message, StackResponses stackResponses) {
		this.ok = ok;
		this.message = message;
		this.stackResponses = stackResponses;
	}

}
