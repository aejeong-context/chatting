package com.chatting.domains.chatting.domain;

import com.chatting.domains.auction.domain.AuctionEntity;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room")
@Entity
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    private String name;

//    @ManyToOne
//    @JoinColumn(name = "auction_id")
//    private AuctionEntity auctionEntity;


}
