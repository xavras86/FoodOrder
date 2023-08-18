ALTER TABLE owner
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES food_ordering_user (user_id);

ALTER TABLE customer
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES food_ordering_user (user_id);

insert into food_ordering_user (user_name, email, password, active) values ('ustomer', 'customer@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into food_ordering_user (user_name, email, password, active) values ('owner', 'owner@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into food_ordering_role (role_id, role) values (1, 'CUSTOMER'), (2, 'OWNER');

UPDATE owner SET user_id = 1 WHERE owner_id = 1;
UPDATE customer SET user_id = 2 WHERE customer_id = 1;

insert into food_ordering_user_role (user_id, role_id) values (1, 1);
insert into food_ordering_user_role (user_id, role_id) values (2, 2);
