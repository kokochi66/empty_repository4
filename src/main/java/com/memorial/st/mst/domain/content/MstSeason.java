package com.memorial.st.mst.domain.content;

import com.memorial.st.mst.domain.content.enumType.SeasonType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mst_season")
public class MstSeason implements Serializable {
    private static final long serialVersionUID = 7630412837576971775L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private MstContent content;

    @Enumerated(EnumType.STRING)
    private SeasonType season;
}
