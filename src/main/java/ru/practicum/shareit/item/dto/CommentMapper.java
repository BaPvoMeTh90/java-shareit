package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentOutputDto toCommentOutputDto(Comment comment) {
        CommentOutputDto commentOutputDto = new CommentOutputDto();
        commentOutputDto.setId(comment.getId());
        commentOutputDto.setText(comment.getText());
        commentOutputDto.setItem(comment.getItem());
        commentOutputDto.setAuthorName(comment.getAuthor().getName());
        commentOutputDto.setCreated(comment.getCreated());

        return commentOutputDto;
    }

    public static Comment toComment(CommentInputDto commentInputDto) {
        Comment comment = new Comment();
        comment.setText(commentInputDto.getText());
        return comment;
    }
}