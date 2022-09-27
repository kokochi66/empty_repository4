package com.memorial.st.mst.domain.content;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mst_highlight")
public class MstHighlight implements Serializable {

    private static final long serialVersionUID = 8873615805124886722L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private MstContent content;

    private Integer time;
}
