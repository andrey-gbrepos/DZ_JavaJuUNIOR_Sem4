package ru.gb.lesson4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.entity.Post;
import ru.gb.lesson4.entity.PostComment;

public class JPAMain {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.configure(); // !!! иначе cfg.xml не прочитается
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            // Добавление строк в таблицу post и post_comment
            addRowPostTable(sessionFactory);
            addRowPostCommentTable(sessionFactory);
            //изменение таблицы post
            updatePost(sessionFactory, 3L);
            //удаление из таблицы post_comment
            deleteComment(sessionFactory, 2L);
        }
    }


    private static void addRowPostTable(SessionFactory sessionFactory) {

        String[] strTitle = {"Hobby", "Profession", "Weather"};
        for (int i = 0; i < 3; i++) {

            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                Post post = new Post();
                post.setId((long) i + 1);
                post.setTitle(strTitle[i]);
                session.persist(post);
                tx.commit();
            }
        }
    }

    private static void addRowPostCommentTable(SessionFactory sessionFactory) {

        Long[] longPostId = {2L, 3L, 2L, 1L, 1L};
        String[] strComment = {"ljlj'jbmb jkjh", "jbmbmb h;h;", "iookljlkbmbmb", ",mbvm,vjsid up", "oiqweft xcvbdf"};
        for (int i = 0; i < 5; i++) {

            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                PostComment comment = new PostComment();
                comment.setId((long) i + 1);
                comment.setText(strComment[i]);
                comment.setPost_id(longPostId[i]);
                session.persist(comment);
                tx.commit();
            }
        }
    }

    private static void updatePost(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.find(Post.class, id);
            session.detach(post);
            post.setTitle("UPDATED");
            Transaction tx = session.beginTransaction();
            session.merge(post); // update
            tx.commit();
        }
    }

    private static void deleteComment(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            PostComment comment = session.find(PostComment.class, id);
            Transaction tx = session.beginTransaction();
            session.remove(comment); // delete
            tx.commit();
        }
    }
}
