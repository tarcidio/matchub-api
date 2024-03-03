package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTODetails extends CommentDTOBase{
    private HubUserDTOLinks hubUser;
    private ScreenDTOLinks screenId;
    private List<EvaluationDTOLinks> evaluations = new ArrayList<>();
}
