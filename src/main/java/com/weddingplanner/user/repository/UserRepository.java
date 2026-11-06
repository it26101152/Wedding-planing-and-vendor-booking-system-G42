package com.weddingplanner.user.repository;
import com.weddingplanner.user.model.User;
import com.weddingplanner.common.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository public class UserRepository extends FileRepository<User>
{
    public UserRepository(@Value("${app.data.dir:data}") String dir)
    {
        super(dir,"users.txt","USR");
    }
    @Override protected User fromFields(String[] f)
    {
        if(f.length<4) throw new IllegalArgumentException("Invalid record");
        User e=new User();
        e.setName(f[0]);
        e.setEmail(f[1]);
        e.setPasswordHash(f[2]);
        e.setRole(f[3]);
        return e;
    }
}
