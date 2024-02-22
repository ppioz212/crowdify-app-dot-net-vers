using Microsoft.AspNetCore.Mvc;
using Capstone.DAO;
using Capstone.Exceptions;
using Capstone.Models;
using Capstone.Security;

namespace Capstone.Controllers
{
    [Route("[controller]")]
    [ApiController]
    public class Campaigncontroller : ControllerBase
    {
        private readonly ITokenGenerator tokenGenerator;
        private readonly IPasswordHasher passwordHasher;
        private readonly IUserDao userDao;

        public Campaigncontroller(ITokenGenerator tokenGenerator, IPasswordHasher passwordHasher, IUserDao userDao)
        {
            this.tokenGenerator = tokenGenerator;
            this.passwordHasher = passwordHasher;
            this.userDao = userDao;
        }
    }
}
