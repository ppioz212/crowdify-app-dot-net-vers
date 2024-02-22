using System;

namespace Capstone.Models
{
    public class Donation
    {
        public int Id { get; set; }
        public string CampaignName { get; set; }
        public User Donor { get; set; }
        public int CampaignId { get; set; }
        public string Name { get; set; }
        public int Amount { get; set; }
        public DateTime date { get; set; }
        public string Comment { get; set; }
        public bool IsRefunded { get; set; }
        public bool IsAnonymous { get; set; }

        public Donation() { }
    }
}
