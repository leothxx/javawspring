package com.spring.javawspring.vo;

import lombok.Data;

public @Data class BoardReplyVO {
	private int idx;
	private int boardIdx;
	private String mid;
	private String nickName;
	private String wDate;
	private String hostIp;
	private String content;
	private int level;
	private int levelOrder;
}
