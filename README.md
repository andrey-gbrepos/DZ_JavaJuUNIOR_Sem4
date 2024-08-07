# DZ_JavaJuUNIOR_Sem4

  /**
   * Используя hibernate, создать таблицы:
   * 1. Post (публикация) (id, title)
   * 2. PostComment (комментарий к публикации) (id, text, post_id)
   *
   * Написать стандартные CRUD-методы: создание, загрузка, удаление.
   *
   * Доп. задания:
   * 1. * В сущностях post и postComment добавить поля timestamp с датами.
   * 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.
   * 3. * Реализовать методы:
   * 3.1 Загрузить все комментарии публикации
   * 3.2 Загрузить все публикации по идентификатору юзера
   * 3.3 ** Загрузить все комментарии по идентификатору юзера
   * 3.4 **** По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты.
   * // userId -> List<User>
   *
   *
   * Замечание:
   * 1. Можно использовать ЛЮБУЮ базу данных (например, h2)
   * 2. Если запутаетесь, приходите в группу в телеграме или пишите мне @inchestnov в личку.
   */

# Подготовка 

1. Создали стандартный проект (под maven)
2. Добавили зависимости в pom.xml для hibernate и для базы данных  postgresql
3. В каталоге проекта main/resources создали файл настроек hibernate.cfg.xml:
```
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
	<!-- //путь к драйверу-->
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>  
 	<!-- // параметры подключения-->
        <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/postgres</property> 
       	<!-- //имя пользователя-->
  <property name="hibernate.connection.username">postgres</property> 
 	<!-- // пароль-->      
  <property name="hibernate.connection.password">pass</property> 
 	<!--// для отображения процесса работы с БД в консоли  -->       
 <property name="show_sql">true</property> 

	<!-- Режимы работы hibernate: none, create, create-drop, update, validate-->
        <property name="hibernate.hbm2ddl.auto">create</property>

	<!--сущности для работы с БД-->
        <mapping class="ru.gb.lesson4.entity.Post"/>
        <mapping class="ru.gb.lesson4.entity.PostComment"/>
      
    </session-factory>
</hibernate-configuration>
````
4. На компе запустили докер с postgresql и входим для контроля командой:
docker exec -it postgres psql -U postgres


# Выполнение
   ## Используя hibernate, создать таблицы:
   ## 1. Post (публикация) (id, title)
   ## 2. PostComment (комментарий к публикации) (id, text, post_id)

* Создали два класса сущностей
```
@Entity
@Table(name = "post")
public class Post {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String title;
    
   + getters and setters
}

@Entity
@Table(name = "post_comment")
public class PostComment {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "post_id")
    private Long post_id;

+ getters and setters
}
```
* Создаем класс JPAMain в котором создаем методы для заполнения таблиц post и post_comment, изменения записей и удаления строк по ID.
``` 
 private static void addRowPostTable(SessionFactory sessionFactory) {
        String[] strTitle = {"Hobby", "Profession", "Weather"};
        for (int i = 0; i < 3; i++) {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                Post post = new Post();
                post.setId((long) i+1);
                post.setTitle(strTitle[i]);
                posts.add(post);
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
                comment.setId((long) i+1);
                comment.setText(strComment[i]);
                comment.setPost_id(longPostId[i]);
                comments.add(comment);
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
            session.merge(toUpdate); // update
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
    ```
* В методе Main прописываем следущий код:
```
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
```
* В файле конфигурации hibernate.cfg.xml в строке
  <property name="hibernate.hbm2ddl.auto">create</property> 
  контролируем режим "create" и запускаем Main.
Лог выполнения:
```
Hibernate: drop table if exists post cascade

Hibernate: drop table if exists post_comment cascade

Hibernate: create table post (id bigint not null, name varchar(255), primary key (id))


Hibernate: create table post_comment (id bigint not null, post_id bigint, text varchar(255), primary key (id))

Hibernate: insert into post (name,id) values (?,?)

Hibernate: insert into post (name,id) values (?,?)

Hibernate: insert into post (name,id) values (?,?)

Hibernate: insert into post_comment (post_id,text,id) values (?,?,?)

Hibernate: insert into post_comment (post_id,text,id) values (?,?,?)

Hibernate: insert into post_comment (post_id,text,id) values (?,?,?)

Hibernate: insert into post_comment (post_id,text,id) values (?,?,?)

Hibernate: insert into post_comment (post_id,text,id) values (?,?,?)

Hibernate: select p1_0.id,p1_0.name from post p1_0 where p1_0.id=?

Hibernate: select p1_0.id,p1_0.name from post p1_0 where p1_0.id=?

Hibernate: update post set name=? where id=?

Hibernate: select pc1_0.id,pc1_0.post_id,pc1_0.text from post_comment pc1_0 where pc1_0.id=?

Hibernate: delete from post_comment where id=?
```
-----
## Доп. задания:
##    1. * В сущностях post и postComment добавить поля timestamp с датами.

* В классы - сущности Post PostCommentдобавляем поля timestamp ( класс java.sql.Timestamp). Добавляем getters-setters.

```
    @Column(name = "timestamp")
    private Timestamp timestamp;
```

* В файле конфигурации hibernate.cfg.xml в строке
  <property name="hibernate.hbm2ddl.auto">update</property> 
 устанавливаем режим "update" и запускаем Main.


 В логе:
 ```
Hibernate: alter table if exists post add column timestamp timestamp(6)

Hibernate: alter table if exists post_comment add column timestamp timestamp(6)
 ```

## 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.


* Создаем класс-сущность Users
```
@Entity
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String name; 

    + getters и setters
}
```
* В классах Post и PostComment добавляем поля со ссылкой на users:
```
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    + getter и setter
```
* В файле конфигурации hibernate.cfg.xml в строке
  <property name="hibernate.hbm2ddl.auto">create</property> 
 устанавливаем режим "create", добавляем строку с сущностью User:
 <mapping class="ru.gb.lesson4.entity.Users"/>
  запускаем Main.

  Лог:
  ```
Hibernate: alter table if exists post drop constraint if exists FK7ky67sgi7k0ayf22652f7763r

Hibernate: alter table if exists post_comment drop constraint if exists FKbh2kvd72ce49c3f0bj77rxji2

Hibernate: drop table if exists post cascade

Hibernate: drop table if exists post_comment cascade

Hibernate: drop table if exists users cascade

Hibernate: create table post (currentDate timestamp(6), id bigint not null, timestamp timestamp(6), user_id bigint, name varchar(255), primary key (id))

Hibernate: create table post_comment (currentDate timestamp(6), id bigint not null, post_id bigint, timestamp timestamp(6), user_id bigint, text varchar(255), primary key (id))

Hibernate: create table users (id bigint not null, user_name varchar(255), primary key (id))

Hibernate: alter table if exists post add constraint FK7ky67sgi7k0ayf22652f7763r foreign key (user_id) references users

Hibernate: alter table if exists post_comment add constraint FKbh2kvd72ce49c3f0bj77rxji2 foreign key (user_id) references users

Hibernate: insert into post (currentDate,timestamp,name,user_id,id) values (?,?,?,?,?)

Hibernate: insert into post (currentDate,timestamp,name,user_id,id) values (?,?,?,?,?)

Hibernate: insert into post (currentDate,timestamp,name,user_id,id) values (?,?,?,?,?)

Hibernate: insert into post_comment (currentDate,post_id,text,timestamp,user_id,id) values (?,?,?,?,?,?)

Hibernate: insert into post_comment (currentDate,post_id,text,timestamp,user_id,id) values (?,?,?,?,?,?)

Hibernate: insert into post_comment (currentDate,post_id,text,timestamp,user_id,id) values (?,?,?,?,?,?)

Hibernate: insert into post_comment (currentDate,post_id,text,timestamp,user_id,id) values (?,?,?,?,?,?)

Hibernate: insert into post_comment (currentDate,post_id,text,timestamp,user_id,id) values (?,?,?,?,?,?)

Hibernate: select p1_0.id,p1_0.currentDate,p1_0.timestamp,p1_0.name,u1_0.id,u1_0.user_name from post p1_0 left join users u1_0 on u1_0.id=p1_0.user_id where p1_0.id=?

Hibernate: select p1_0.id,p1_0.currentDate,p1_0.timestamp,p1_0.name,u1_0.id,u1_0.user_name from post p1_0 left join users u1_0 on u1_0.id=p1_0.user_id where p1_0.id=?

Hibernate: update post set currentDate=?,timestamp=?,name=?,user_id=? where id=?

Hibernate: select pc1_0.id,pc1_0.currentDate,pc1_0.post_id,pc1_0.text,pc1_0.timestamp,u1_0.id,u1_0.user_name from post_comment pc1_0 left join users u1_0 on u1_0.id=pc1_0.user_id where pc1_0.id=?

Hibernate: delete from post_comment where id=?
  ```
