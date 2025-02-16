CREATE TABLE Shared_File_Info(
	shared_file_info_id int GENERATED ALWAYS AS IDENTITY,
	name varchar(50) NOT NULL,
	byteSize bigint,
	user_id int,

	CONSTRAINT PK_shared_file_info PRIMARY KEY (shared_file_info_id)
);


ALTER TABLE Shared_File_Info
ADD CONSTRAINT FK_Shared_File_Info__Users foreign key (user_id) references Users (user_id);