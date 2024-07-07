package com.github.brdr3.awsresiliencepoc.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DomainMessage {
    private UUID id;
    private String content;
}
