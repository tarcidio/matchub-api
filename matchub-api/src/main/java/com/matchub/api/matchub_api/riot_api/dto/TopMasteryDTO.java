package com.matchub.api.matchub_api.riot_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopMasteryDTO {
    private String puuid;//Player Universal Unique Identifier. Exact length of 78 characters. (Encrypted)
    private Long championPointsUntilNextLevel;//Number of points needed to achieve next level. Zero if player reached maximum champion level for this champion.
    private Boolean chestGranted;//Is chest granted for this champion or not in current season.
    private Long championId; //Champion ID for this entry.
    private Long lastPlayTime;//Last time this champion was played by this player - in Unix milliseconds time format.
    private Integer championLevel;//Champion level for specified player and champion combination.
    private String summonerId;// Summoner ID for this entry. (Encrypted)
    private Integer championPoints;// Total number of champion points for this player and champion combination - they are used to determine championLevel.
    private Long championPointsSinceLastLevel;// Number of points earned since current level has been achieved.
    private Integer tokensEarned;// The token earned for this champion at the current championLevel. When the championLevel is advanced the tokensEarned resets to 0.
}
