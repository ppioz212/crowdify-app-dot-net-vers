using Microsoft.VisualBasic;
using System;
using System.Collections;
using System.Collections.Generic;

namespace Capstone.Models
{
    public class Campaign
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateAndTime StartDate { get; set; }
        public DateAndTime EndDate { get; set; }
        public bool IsLocked { get; set; }
        public bool IsPublic { get; set; }
        public IList<Donation> Donations = new List<Donation>();
        public User Creator { get; set; }
        public bool IsDeleted { get; set; }


        public Campaign() { }

    }
}
