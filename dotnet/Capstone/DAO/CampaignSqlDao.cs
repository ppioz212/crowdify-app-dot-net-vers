using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using Capstone.Exceptions;
using Capstone.Models;
using Capstone.Security;
using Capstone.Security.Models;

namespace Capstone.DAO
{
    public class CampaignSqlDao
    {
        private readonly string connectionString;

        public CampaignSqlDao(string dbConnectionString)
        {
            connectionString = dbConnectionString;
        }


    }
}