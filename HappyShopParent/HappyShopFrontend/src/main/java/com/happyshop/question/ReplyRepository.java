package com.happyshop.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{

}
